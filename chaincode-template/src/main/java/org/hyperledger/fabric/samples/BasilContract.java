/*
 * SPDX-License-Identifier: Apache-2.0
 */

package org.hyperledger.fabric.samples;

import com.owlike.genson.Genson;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.*;
import org.hyperledger.fabric.samples.models.Basil;
import org.hyperledger.fabric.samples.models.BasilLeg;
import org.hyperledger.fabric.samples.models.Owner;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyModification;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Contract(name = "basic")
@Default
public final class BasilContract implements ContractInterface {

    public enum ReturnValues {
        ALREADY_EXISTING("ALREADY EXISTING"),
        DOES_NOT_EXIST("DOES NOT EXIST"),
        UNAUTHORIZED("UNAUTHORIZED");


        private final String value;

        ReturnValues(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    private final Genson genson = new Genson();

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String CreateTracking(final Context context, final String qr, final Basil basil) {
        ChaincodeStub stub = context.getStub();

        // the supermarket cannot execute this transaction
        if (context.getClientIdentity().getMSPID().equals("Org2MSP")) {
            return ReturnValues.UNAUTHORIZED.value;
        }

        if (basilExists(context, qr)) {
            return ReturnValues.ALREADY_EXISTING.value; // we could use an enum
        }

        // id = x509::ksdjfjdsf::aiodaj
        Owner owner = new Owner(context.getClientIdentity().getMSPID(), getUser(context));
        basil.setOwner(owner);
        BasilLeg basilLeg = new BasilLeg(basil, "-1,-1", stub.getTxTimestamp().toString());
        String serialized = genson.serialize(basilLeg);
        stub.putStringState(qr, serialized);
        return serialized;
    }

    // TODO implement all other transactions

    private static String getUser(Context context) {
        return context.getClientIdentity().getId().split("::")[1].split(",")[0].split("=")[1];
    }

    public boolean basilExists(Context context, String qr) {
        String stringState = context.getStub().getStringState(qr);
        return stringState != null && !stringState.isEmpty();
    }

    private boolean checkStringBasilLeg(String assetJSON) {
        return assetJSON != null && !assetJSON.isEmpty();
    }

    private static String getBasilLeg(Context ctx, String qr) {
        ChaincodeStub stub = ctx.getStub();
        return stub.getStringState(qr);
    }

    /**
     *
     * @return true if the BasilLeg exists, false otherwise
     */
    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public boolean Query(Context ctx, String qr) {
        String assetJSON = getBasilLeg(ctx, qr);
        return checkStringBasilLeg(assetJSON);
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String QueryStringBasilLeg(final Context ctx, final String qr) {
        if (ctx.getClientIdentity().getMSPID().equals("Org2MSP")) {
            return ReturnValues.UNAUTHORIZED.value;
        }
//        String basilLegJSON = getBasilLeg(ctx, qr);
//        if(checkStringBasilLeg(basilLegJSON)){
//            return basilLegJSON;
//        }
//        return ReturnValues.DOES_NOT_EXIST.value;
        return getBasilLeg(ctx, qr);
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String Transfer(final Context ctx,
                           final String qr,
                           final Owner newOwner) {

        if (ctx.getClientIdentity().getMSPID().equals("Org2MSP")) {
            return ReturnValues.UNAUTHORIZED.value;
        }

        if(newOwner.getOrg().equals("Org2MSP")){
            return ReturnValues.UNAUTHORIZED.value;
        }

        ChaincodeStub stub = ctx.getStub();
        String basilLegJSON = getBasilLeg(ctx, qr);
        if (checkStringBasilLeg(basilLegJSON)) {

            BasilLeg basilLeg = genson.deserialize(basilLegJSON, BasilLeg.class);
            // only the car's owner can transfer the ownership
            //Prendi org and user di utente che fa modifica
            // persona che sta facendo richiesta di trasferimento
            //Owner requester = new Owner(ctx.getClientIdentity().getMSPID(), getUser(ctx));
            Basil basil = basilLeg.getBasil();
            //if (basil.getOwner().equals(requester)) {
            if(checkOwner(ctx, basil)){
                basil.setOwner(newOwner);
                basilLeg.setBasil(basil);
                String basilLegReturnJSON = genson.serialize(basilLeg);
                stub.putStringState(qr, basilLegReturnJSON);
                return basilLegReturnJSON;
            } else {
                return ReturnValues.UNAUTHORIZED.value;
            }
        } else {
            return ReturnValues.DOES_NOT_EXIST.value;
        }
    }

    private boolean checkOwner(Context ctx, Basil basil){
        Owner requester = new Owner(ctx.getClientIdentity().getMSPID(), getUser(ctx));
        if(basil.getOwner().equals(requester)){
            return true;
        }
        return false;
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String DeleteTracking(final Context ctx, final String qr){
        if (ctx.getClientIdentity().getMSPID().equals("Org2MSP")) {
            return ReturnValues.UNAUTHORIZED.value;
        }

        ChaincodeStub stub = ctx.getStub();
        String basilLegJSON = getBasilLeg(ctx, qr);
        if (checkStringBasilLeg(basilLegJSON)) {
            BasilLeg basilLeg = genson.deserialize(basilLegJSON, BasilLeg.class);
            if(checkOwner(ctx, basilLeg.getBasil())){
                stub.delState(qr);
                return basilLegJSON;
            }else{
                return ReturnValues.UNAUTHORIZED.value;
            }
        }else{
            return ReturnValues.DOES_NOT_EXIST.value;
        }
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)                   //added later
    public boolean Sell(final Context ctx, final String qr){
        if (ctx.getClientIdentity().getMSPID().equals("Org2MSP")) {

            ChaincodeStub stub = ctx.getStub();
            String basilLegJSON = getBasilLeg(ctx, qr);
            if (checkStringBasilLeg(basilLegJSON)) {
                BasilLeg basilLeg = genson.deserialize(basilLegJSON, BasilLeg.class);
                if(checkOwner(ctx, basilLeg.getBasil())){
                    stub.delState(qr);
                    return true;
                }else{
                     return false;
                }
            }else{
                 return false;
            }
        }
        else{
            return false;
        }
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String GetHistory(final Context ctx, final String qr){
        //List<BasilLeg> list = new ArrayList<>();
        StringBuilder str = new StringBuilder("");
        ChaincodeStub stub = ctx.getStub();
        QueryResultsIterator<KeyModification> historyForKey = stub.getHistoryForKey(qr);
        for (KeyModification keyModification : historyForKey) {
            str.append(keyModification.getStringValue());
            str.append("\n");
        }
        return str.toString();
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String UpdateTracking(final Context ctx, final String qr, final BasilLeg newBasilStatus){
        if (ctx.getClientIdentity().getMSPID().equals("Org2MSP")) {
            return ReturnValues.UNAUTHORIZED.value;
        }

        ChaincodeStub stub = ctx.getStub();
        String basilLegJSON = getBasilLeg(ctx, qr);
        if (checkStringBasilLeg(basilLegJSON)) {
            BasilLeg basilLeg = genson.deserialize(basilLegJSON, BasilLeg.class);
            if(checkOwner(ctx, basilLeg.getBasil())){
                newBasilStatus.setTimestamp(stub.getTxTimestamp().toString());
                String serialized = genson.serialize(newBasilStatus);
                stub.putStringState(qr, serialized);
                return serialized;
            }else{
                return ReturnValues.UNAUTHORIZED.value;
            }
        }else{
            return ReturnValues.DOES_NOT_EXIST.value;
        }
    }
}
