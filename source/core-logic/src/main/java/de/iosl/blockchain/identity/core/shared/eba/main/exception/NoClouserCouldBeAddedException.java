package de.iosl.blockchain.identity.core.shared.eba.main.exception;

import de.iosl.blockchain.identity.core.shared.eba.contracts.Clouser_sol_ClouserContract;
import org.web3j.tx.Contract;

public class NoClouserCouldBeAddedException extends Exception {

    Clouser_sol_ClouserContract contract;
    public NoClouserCouldBeAddedException(String message, Clouser_sol_ClouserContract contract) {
        super(message);
        this.contract=contract;
    }

    public Clouser_sol_ClouserContract getContract(){
        return contract;
    }
}

