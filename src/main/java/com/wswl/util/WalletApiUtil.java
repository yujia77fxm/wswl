package com.wswl.util;

import com.wswl.entity.AddressEntity;
import org.bouncycastle.jcajce.provider.digest.Keccak;
import org.bouncycastle.util.encoders.Hex;
import org.tron.api.GrpcAPI;
import org.tron.common.utils.Utils;
import org.tron.core.exception.CancelException;
import org.tron.core.exception.CipherException;
import org.tron.protos.Protocol;
import org.tron.walletcli.WalletApiWrapper;
import org.tron.walletserver.WalletApi;

import java.io.IOException;

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
    /**
     * TransferAsset [OwnerAddress] ToAddress AssertID Amount
     * @param parameters = ["OwnerAddress",
     *                      "ToAddress",
     *                      "AssertID",
     *                      "Amount"]
     *
     * */
    public String transferAsset(String[] parameters)
            throws IOException, CipherException, CancelException {
        if (parameters == null || (parameters.length != 3 && parameters.length != 4)) {
            System.out.println("TransferAsset needs 3 parameters using the following syntax: ");
            System.out.println("TransferAsset [OwnerAddress] ToAddress AssertID Amount");
            return "TransferAsset needs 3 parameters using the following syntax: \n"+
                    "TransferAsset [OwnerAddress] ToAddress AssertID Amount";
        }

        int index = 0;
        byte[] ownerAddress = null;
        if (parameters.length == 4) {
            ownerAddress = WalletApi.decodeFromBase58Check(parameters[index++]);
            if (ownerAddress == null) {
                return "Invalid OwnerAddress.";
            }
        }

        String base58Address = parameters[index++];
        byte[] toAddress = WalletApi.decodeFromBase58Check(base58Address);
        if (toAddress == null) {
            return "Invalid toAddress.";
        }
        String assertName = parameters[index++];
        String amountStr = parameters[index++];
        long amount = new Long(amountStr);

        boolean result = walletApiWrapper.transferAsset(ownerAddress, toAddress, assertName, amount);
        if (result) {
            return "TransferAsset " + amount + " to " + base58Address + " successful !!";
        } else {
            return "TransferAsset " + amount + " to " + base58Address + " failed !!";
        }
    }

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
     *
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

        System.out.println("Base58Check: " + Base58Check.bytesToBase58(rawAddr));
        System.out.println("Hex Address: " + Hex.toHexString(rawAddr));
        System.out.println("Public Key:  " + Hex.toHexString(pubKey.getEncoded()));
        System.out.println("Private Key: " + Hex.toHexString(kp.getPrivateKey().getEncoded()));

        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setBase58Check(Base58Check.bytesToBase58(rawAddr));
        addressEntity.setHexAddress(Hex.toHexString(rawAddr));
        addressEntity.setPrivateKey(Hex.toHexString(kp.getPrivateKey().getEncoded()));
        addressEntity.setPublicKey(Hex.toHexString(pubKey.getEncoded()));
        return addressEntity;
    }

}



