pragma solidity ^0.4.11;

contract ClouserContract{

    address private owner;
    address private user;
    string [] private clouserContents;
    bool private setAllClaims;
    uint private amountClousers;

    function ClouserContract(address _user, uint _amountClousers) public{
        owner=msg.sender;
        user=_user;
        setAllClaims=false;
        amountClousers=_amountClousers;
    }

    function getsetAllClaims() public constant returns(bool){
        return setAllClaims;
    }

     function getClouserById(uint index) public constant returns(string){
        return clouserContents[index];
    }

    function getRequestedClouserAsUserAndDeleteIndex(uint index) ifUser public returns(string) {
        string storage requestedClouser = clouserContents[index];
        if (index >= clouserContents.length) return;

        for (uint i = index; i<clouserContents.length-1; i++){
            clouserContents[i] = clouserContents[i+1];
        }
        delete clouserContents[clouserContents.length-1];
        clouserContents.length--;
        return requestedClouser;
    }

    function addClouserAsUser(string clouserContent) public ifUser (){
        clouserContents.push(clouserContent);
    }

    function addClouserAsRequestingProvider(string clouserContent) public ifOwner (){
        clouserContents.push(clouserContent);
    }

    function kill() public ifOwner{
        selfdestruct(owner);
    }


    //modifier
    modifier ifUser(){
        if(user != msg.sender){
            return;
        }
        else{
            _;
        }
    }

    modifier ifOwner(){
        if(owner != msg.sender){
            return;
        }
        else{
            _;
        }
    }


}