package de.iosl.blockchain.identity.core.shared.eba.main.exception;

import de.iosl.blockchain.identity.core.shared.eba.contracts.Closure_sol_ClosureContract;

public class NoClouserCouldBeAddedException extends Exception {

    Closure_sol_ClosureContract contract;
    public NoClouserCouldBeAddedException(String message, Closure_sol_ClosureContract contract) {
        super(message);
        this.contract=contract;
    }

    public Closure_sol_ClosureContract getContract(){
        return contract;
    }
}

