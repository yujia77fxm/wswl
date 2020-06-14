package com.wswl.util;

import com.google.protobuf.Any;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.wswl.entity.AddressEntity;
import com.wswl.entity.TransferResult;
import org.bouncycastle.jcajce.provider.digest.Keccak;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.tron.api.GrpcAPI;
import org.tron.common.crypto.ECKey;
import org.tron.common.crypto.Sha256Sm3Hash;
import org.tron.common.utils.ByteArray;
import org.tron.common.utils.Utils;
import org.tron.core.exception.CancelException;
import org.tron.core.exception.CipherException;
import org.tron.protos.Protocol;
import org.tron.protos.contract.AssetIssueContractOuterClass;
import org.tron.protos.contract.BalanceContract;
import org.tron.walletcli.WalletApiWrapper;
import org.tron.walletserver.WalletApi;

import java.io.IOException;
import java.util.Arrays;

import static org.tron.walletserver.WalletApi.createTransferAssetContract;

public class WalletApiUtil {
    private static final WalletApiUtil instance = new WalletApiUtil();
    WalletApiWrapper walletApiWrapper = null;

    private WalletApiUtil(){
         walletApiWrapper = new WalletApiWrapper();
    }

    public static WalletApiUtil getInstance(){
        return instance;
    }
    public String GetAddress(String address ){
        //String address = "TWbcHNCYzqAGbrQteKnseKJdxfzBHyTfuh";
        byte[] addressBytes = WalletApi.decodeFromBase58Check(address);
        if (addressBytes == null) {
            return "address parameter not available!! ";
        }

        Protocol.Account account = WalletApi.queryAccount(addressBytes);
        if (account == null) {
            System.out.println("GetAccount failed !!!!");
            return "account == null----";
        } else {
            System.out.println(Utils.formatMessageString(account));
            return account.toString() + "  show account";
        }
    }

    public void createAccount(String[] parameters)
            throws CipherException, IOException, CancelException {

        WalletApiWrapper walletApiWrapper = new WalletApiWrapper();
        if (parameters == null || (parameters.length != 1 && parameters.length != 2)) {
            System.out.println("CreateAccount needs 1 parameter using the following syntax: ");
            System.out.println("CreateAccount [OwnerAddress] Address");
            return;
        }

        int index = 0;
        byte[] ownerAddress = null;
        if (parameters.length == 2) {
            ownerAddress = WalletApi.decodeFromBase58Check(parameters[index++]);
            if (ownerAddress == null) {
                System.out.println("Invalid OwnerAddress.");
                return;
            }
        }

        byte[] address = WalletApi.decodeFromBase58Check(parameters[index++]);
        if (address == null) {
            System.out.println("Invalid Address.");
            return;
        }

        boolean result = walletApiWrapper.createAccount(ownerAddress, address);
        if (result) {
            System.out.println("CreateAccount successful !!");
        } else {
            System.out.println("CreateAccount failed !!");
        }
    }


    /***
     * 节点生成地址
     * @return
     */
    public String generateAddress() {
        //GrpcAPI.AddressPrKeyPairMessage result = walletApiWrapper.generateAddress();
        GrpcAPI.AddressPrKeyPairMessage result = walletApiWrapper.generateAddressWithoutLogin();
        if (null != result) {
            System.out.println(Utils.formatMessageString(result));
            return Utils.formatMessageString(result);
        } else {
            System.out.println("GenerateAddress failed !!!");
            return "GenerateAddress failed !!!";
        }
    }

    /**
     * 本地生成地址
     * @param
     */
    public static AddressEntity generateLocalAddress() {
        // generate random address
        SECP256K1.KeyPair kp = SECP256K1.KeyPair.generate();

        SECP256K1.PublicKey pubKey = kp.getPublicKey();
        Keccak.Digest256 digest = new Keccak.Digest256();
        digest.update(pubKey.getEncoded(), 0, 64);
        byte[] raw = digest.digest();
        byte[] rawAddr = new byte[21];
        rawAddr[0] = 0x41;
        System.arraycopy(raw, 12, rawAddr, 1, 20);

        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setBase58Check(Base58Check.bytesToBase58(rawAddr));
        addressEntity.setHexAddress(Hex.toHexString(rawAddr));
        addressEntity.setPrivateKey(Hex.toHexString(kp.getPrivateKey().getEncoded()));
        addressEntity.setPublicKey(Hex.toHexString(pubKey.getEncoded()));
        return addressEntity;
    }

    /**
     * 获取交易id
     * @param transaction
     * @return
     */
    public static String getTransactionHash(Protocol.Transaction transaction) {
        String txid = ByteArray.toHexString(Sha256Sm3Hash.hash(transaction.getRawData().toByteArray()));
        return txid;
    }
    /**
     * @desc  转账 TRX
     * @param fromAddress
     * @param toAddress
     * @param privateKey
     * @param amount
     */
    public static TransferResult transferAsset(String fromAddress, String toAddress, String privateKey, long amount)throws InvalidProtocolBufferException, CancelException{
        //String privateStr = "D95611A9AF2A2A45359106222ED1AFED48853D9A44DEFF8DC7913F5CBA727366";
        //long amount = 100_000_000L; // 100 TRX, api only receive trx in Sun, and 1 trx = 1000000 Sun
        //byte[] to = WalletApi.decodeFromBase58Check("TGehVcNhud84JDCGrNHKVz9jEAVKUpbuiv");
        byte[] privateBytes = ByteArray.fromHexString(privateKey);
        ECKey ecKey = ECKey.fromPrivate(privateBytes);
        byte[] from = ecKey.getAddress();//fromAddress.getBytes();//
        byte[] to = WalletApi.decodeFromBase58Check(toAddress);

        TransferResult result = new TransferResult();
        // 生成交易
        Protocol.Transaction transaction = createTransaction(from, to, amount);
        byte[] transactionBytes = transaction.toByteArray();

        String txid = getTransactionHash(transaction);
        result.setTxid(txid);
        // 对交易签名并以byte返回交易
        byte[] transaction4 = signTransaction2Byte(transactionBytes, privateBytes);

        boolean broadResult = broadcast(transaction4);
        result.setResult(broadResult);

        return result;
    }

    /**
     *
     * @param transaction
     * @param privateKey
     * @return
     * @throws InvalidProtocolBufferException
     */
    private static byte[] signTransaction2Byte(byte[] transaction, byte[] privateKey)
            throws InvalidProtocolBufferException {
        ECKey ecKey = ECKey.fromPrivate(privateKey);
        Protocol.Transaction transaction1 = Protocol.Transaction.parseFrom(transaction);
        byte[] rawdata = transaction1.getRawData().toByteArray();
        byte[] hash = Sha256Sm3Hash.hash(rawdata);
        byte[] sign = ecKey.sign(hash).toByteArray();
        return transaction1.toBuilder().addSignature(ByteString.copyFrom(sign)).build().toByteArray();
    }

    /***
     * @param transactionBytes
     * @return
     * @throws InvalidProtocolBufferException
     */
    public static boolean broadcast(byte[] transactionBytes) throws InvalidProtocolBufferException {
        return WalletApi.broadcastTransaction(transactionBytes);
    }

    /**
     *
     * @param from
     * @param to
     * @param amount
     * @return
     */
    public static Protocol.Transaction createTransaction(byte[] from, byte[] to, long amount) {
        Protocol.Transaction.Builder transactionBuilder = Protocol.Transaction.newBuilder();
        Protocol.Block newestBlock = WalletApi.getBlock(-1);

        Protocol.Transaction.Contract.Builder contractBuilder = Protocol.Transaction.Contract.newBuilder();
        BalanceContract.TransferContract.Builder transferContractBuilder = BalanceContract.TransferContract.newBuilder();
        transferContractBuilder.setAmount(amount);
        ByteString bsTo = ByteString.copyFrom(to);
        ByteString bsOwner = ByteString.copyFrom(from);
        transferContractBuilder.setToAddress(bsTo);
        transferContractBuilder.setOwnerAddress(bsOwner);
        try {
            Any any = Any.pack(transferContractBuilder.build());
            contractBuilder.setParameter(any);
        } catch (Exception e) {
            return null;
        }
        contractBuilder.setType(Protocol.Transaction.Contract.ContractType.TransferContract);
        transactionBuilder.getRawDataBuilder().addContract(contractBuilder)
                .setTimestamp(System.currentTimeMillis())
                .setExpiration(newestBlock.getBlockHeader().getRawData().getTimestamp() + 10 * 60 * 60 * 1000);
        Protocol.Transaction transaction = transactionBuilder.build();
        Protocol.Transaction refTransaction = setReference(transaction, newestBlock);
        return refTransaction;
    }

    /***
     *
     * @param transaction
     * @param newestBlock
     * @return
     */
    public static Protocol.Transaction setReference(Protocol.Transaction transaction, Protocol.Block newestBlock) {
        long blockHeight = newestBlock.getBlockHeader().getRawData().getNumber();
        byte[] blockHash = getBlockHash(newestBlock).getBytes();
        byte[] refBlockNum = ByteArray.fromLong(blockHeight);
        Protocol.Transaction.raw rawData = transaction.getRawData().toBuilder()
                .setRefBlockHash(ByteString.copyFrom(ByteArray.subArray(blockHash, 8, 16)))
                .setRefBlockBytes(ByteString.copyFrom(ByteArray.subArray(refBlockNum, 6, 8)))
                .build();
        return transaction.toBuilder().setRawData(rawData).build();
    }
    public static Sha256Sm3Hash getBlockHash(Protocol.Block block) {
        return Sha256Sm3Hash.of(block.getBlockHeader().getRawData().toByteArray());
    }
    /**
     *
     * 转账后需要进行广播，通知链上节点
     * @param transactionStr
     * */
    public static String broadcastTransaction(String transactionStr) throws InvalidProtocolBufferException {

        Protocol.Transaction transaction = Protocol.Transaction.parseFrom(ByteArray.fromHexString(transactionStr));
        if (transaction == null || transaction.getRawData().getContractCount() == 0) {
            //System.out.println("Invalid transaction");
            return "Invalid transaction";
        }

        boolean ret = WalletApi.broadcastTransaction(transaction);
        if (ret) {
            return "BroadcastTransaction successful !!!";
        } else {
            return "BroadcastTransaction failed !!!";
        }
    }

    /**
     *
     * @param owner
     * @param to
     * @param asertName
     * @param amount
     * @return
     * @throws CipherException
     * @throws IOException
     * @throws CancelException
     */
    public static TransferResult transferAssetTrc10(String owner, String to, String asertName, long amount,String privatekey)
            throws CipherException, IOException, CancelException {

        byte[] privateBytes = ByteArray.fromHexString(privatekey);
        byte[] owner_address = WalletApi.decodeFromBase58Check(owner);
        byte[] to_address = WalletApi.decodeFromBase58Check(to);
        byte[] assertName = asertName.getBytes();

        Protocol.Transaction transaction = WalletApi.getTransferAssetTranction(owner_address,to_address,assertName,amount);

        TransferResult result = new TransferResult();
        String txid = getTransactionHash(transaction);
        result.setTxid(txid);
        // 对交易签名并以byte返回交易
        byte[] transaction4 = signTransaction2Byte(transaction.toByteArray(), privateBytes);

        boolean broadResult = broadcast(transaction4);
        result.setResult(broadResult);
        return result;
    }
}



