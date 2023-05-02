/*
 * Copyright IBM Corp. All Rights Reserved.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

// Running TestApp: 
// gradle runApp 

package application.java;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import application.java.models.Basil;
import application.java.models.BasilLeg;
import application.java.models.Owner;
import com.google.gson.Gson;
import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;


public class App {

    private static Contract contractOrg1Pittaluga;
    private static Contract contractOrg1Picasso;
    private static Contract contractOrg2;

    private static final Scanner scanner = new Scanner(System.in);

    private static final String[] transactionNames = new String[]{
            "CreateTracking","QueryStringBasilLeg","DeleteTracking","UpdateTracking","Transfer","GetHistory" , "Sell"// todo
    };
    private static final String org2UserName = "supermarketUser";

    private static final Gson gson = new Gson();

    static {
        System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "true");
    }

    // helper function for getting connected to the gateway
    public static Gateway connect(String connectionFile, String userName) throws Exception {
        // Load a file system based wallet for managing identities.
        Path walletPath = Paths.get("wallet");
        Wallet wallet = Wallets.newFileSystemWallet(walletPath);
        // load a CCP
        Path networkConfigPath = Paths.get(connectionFile);

        Gateway.Builder builder = Gateway.createBuilder();
        builder.identity(wallet, userName).networkConfig(networkConfigPath).discovery(true);
        return builder.connect();
    }

    public static void main(String[] args) throws Exception {
        // yes, you can improve the quality of the code
        try {
            String org1PemFile = "ca.org1.example.com-cert.pem";
            String org1Admin = "admin";
            String org1Name = "Org1MSP";
            String org1CaUrl = "https://localhost:7054";

            AdminEnrollment.enroll(org1Admin, org1PemFile, org1Name, org1CaUrl);
            UserRegistration.register(org1Admin, "pittalugaUser", org1PemFile,
                    org1Name, org1CaUrl);
            UserRegistration.register(org1Admin, "picassoUser", org1PemFile,
                    org1Name, org1CaUrl);

            String org2Admin = "admin2";
            String org2PemFile = "ca.org2.example.com-cert.pem";
            String org2Name = "Org2MSP";
            String org2CaUrl = "https://localhost:8054";

            AdminEnrollment.enroll(org2Admin, org2PemFile, org2Name, org2CaUrl);
            UserRegistration.register(org2Admin, org2UserName, org2PemFile,
                    org2Name, org2CaUrl);

        } catch (Exception e) {
            System.err.println(e);
        }

        // connect to the network and invoke the smart contract
        String connectionFileOrg1 = "connection-org1.yaml";
        try (Gateway gatewayOrg1Pittaluga = connect(connectionFileOrg1, "pittalugaUser");
             Gateway gatewayOrg2 = connect("connection-org2.yaml", org2UserName);
             Gateway gatewayOrg1Picasso = connect(connectionFileOrg1, "picassoUser")) {

            Network networkOrg1Pittaluga = gatewayOrg1Pittaluga.getNetwork("mychannel");
            Network networkOrg1Picasso = gatewayOrg1Picasso.getNetwork("mychannel");
            Network networkOrg2 = gatewayOrg2.getNetwork("mychannel");
            contractOrg1Pittaluga = networkOrg1Pittaluga.getContract("basic");
            contractOrg1Picasso = networkOrg1Picasso.getContract("basic");
            contractOrg2 = networkOrg2.getContract("basic");

            byte[] result;

            // obviously, you can do a loop each time asking to continue the interaction
            while (true) {
                String txName = chooseOp();

                switch (txName) {
                    case "CreateTracking":

                        System.out.print("Insert QR: ");
                        String qr = scanner.next();
                        System.out.print("Insert extraInfo: ");
                        String info = scanner.next();

                        // here we can pass networkOrg1Pittaluga or networkOrg2 or networkOrg1Picasso: in chooseOrg() it's the same
                        String orgName = chooseOrg(networkOrg1Pittaluga, "Insert the owner number: ");
                        Pair contractUserPair = chooseUser(orgName);
                        // we could avoid setting the owner, since it is set by the smart contract
                        Owner owner = new Owner(orgName, contractUserPair.userName);
                        Basil basil = new Basil(qr, info, owner);
                        // on the application side we use Gson
                        result = contractUserPair.contract.submitTransaction(txName, qr, gson.toJson(basil));
                        System.out.println("result = " + new String(result));
                        break;
                    // TODO implement the other transactions
                    case "QueryStringBasilLeg":
                    case "GetHistory":
                        System.out.print("Insert QR: ");
                        qr = scanner.next();
                        orgName = chooseOrg(networkOrg1Pittaluga, "Insert the owner number: ");
                        contractUserPair = chooseUser(orgName);
                        result = contractUserPair.contract.evaluateTransaction(txName, qr);
                        System.out.println("result = " + new String(result));
                        break;
                    case "DeleteTracking":
                        System.out.print("Insert QR: ");
                        qr = scanner.next();
                        orgName = chooseOrg(networkOrg1Pittaluga, "Insert the owner number: ");
                        contractUserPair = chooseUser(orgName);
                        result = contractUserPair.contract.submitTransaction(txName, qr);
                        System.out.println("Track deleted = " + new String(result));
                        break;
                    case "Sell":           //added later
                        System.out.print("Insert QR: ");
                        qr = scanner.next();
                        orgName = chooseOrg(networkOrg1Pittaluga, "Insert the owner number: ");
                        contractUserPair = chooseUser(orgName);
                        System.out.print("User Pair:"+contractUserPair);
                        System.out.print("Method Name:"+txName);
                        result = contractUserPair.contract.submitTransaction(txName, qr);
                        System.out.println("Track deleted = " + result);
                        break;
                    case "UpdateTracking":
                        System.out.print("Insert QR: ");
                        qr = scanner.next();
                        System.out.print("Insert extraInfo: ");
                        info = scanner.next();
                        System.out.print("Insert GPS position: ");
                        String gps = scanner.next();
                        orgName = chooseOrg(networkOrg1Pittaluga, "Insert the owner number: ");
                        contractUserPair = chooseUser(orgName);
                        owner = new Owner(orgName, contractUserPair.userName);
                        basil = new Basil(qr, info, owner);
                        // on the application side we use Gson
                        BasilLeg basilLeg = new BasilLeg(basil,gps,"");
                        result = contractUserPair.contract.submitTransaction(txName, qr, gson.toJson(basilLeg));
                        System.out.println("result = " + new String(result));
                        break;
                    case "Transfer":
                        System.out.print("Insert QR: ");
                        qr = scanner.next();
                        orgName = chooseOrg(networkOrg1Pittaluga, "Insert the owner number: ");
                        contractUserPair = chooseUser(orgName);
                        System.out.println("Select new Owner");
                        String newOrgName = chooseOrg(networkOrg1Pittaluga, "Insert the owner number: ");
                        Pair newContractUserPair = chooseUser(newOrgName);
                        Owner newOwner = new Owner(newOrgName, newContractUserPair.userName);
                        result = contractUserPair.contract.submitTransaction(txName, qr, gson.toJson(newOwner));
                        System.out.println("result = " + new String(result));
                        break;
                    default:
                        throw new Exception(String.format("Unknown tx \"%s\"", txName));
                }
            }


        } catch (Exception e) {
            System.err.println(e);
        }

    }


    private static final String[] org1Users = new String[]{
            "pittalugaUser", "picassoUser"
    };
    private static Pair chooseUser(String orgName) throws Exception {
        switch (orgName) {
            case "Org1MSP":
                System.out.println("Choose an interacting user:");
                for (int i = 0; i < org1Users.length; i++) {
                    System.out.println(i + ": " + org1Users[i]);
                }
                String userIdx = scanner.next();
                switch (userIdx) {
                    case "0":
                        Pair pair = new Pair();
                        pair.contract = contractOrg1Pittaluga;
                        pair.userName = org1Users[Integer.parseInt(userIdx)];
                        return pair;
                    case "1":
                        Pair pair1 = new Pair();
                        pair1.contract = contractOrg1Picasso;
                        pair1.userName = org1Users[Integer.parseInt(userIdx)];
                        return pair1;
                    default:
                        throw new Exception("Unknown user index");
                }
            case "Org2MSP":
                Pair pair = new Pair();
                pair.contract = contractOrg2;
                pair.userName = org2UserName;
                return pair;
            default:
                throw new Exception(String.format("Unknown \"%s\" org", orgName));
        }
    }

    private static String chooseOp() {
        System.out.println("Choose a tx");
        for (int i = 0; i < transactionNames.length; i++) {
            System.out.println(i + ": " + transactionNames[i]);
        }
        System.out.print("Insert transaction number: ");
        return transactionNames[scanner.nextInt()];
    }


    private static String chooseOrg(Network network, String prompt) throws Exception {
        System.out.println("Actual organizations in the channel...");
        List<String> orgs = new ArrayList<String>(network.getChannel().getPeersOrganizationMSPIDs());
        for (int i = 0; i < orgs.size(); i++) {
            System.out.println(i + ": " + orgs.get(i));
        }
        System.out.print(prompt + " ");
        int orgNum = scanner.nextInt();
        if (orgNum >= orgs.size()) {
            throw new Exception("Choose an interacting org");
        }
        return orgs.get(orgNum);
    }

    private static class Pair {
        Contract contract;
        String userName;
    }
}
