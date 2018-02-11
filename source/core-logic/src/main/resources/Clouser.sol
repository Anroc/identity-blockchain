pragma solidity ^0.4.11;

contract ClouserContract{

    address private owner;
    address private user;
    address private provider;
    string [] private requestedClouser;
    string [] private approvedClouser;
    bool private setAllClaims;
    uint private amountApprovedClousers;
    uint private amountRequestedClousers;
    string private encryptedKey;

    function ClouserContract(address _user,address _requestingProvider, uint _amountRequestedClousers, string _encryptedKey) public{
        owner=msg.sender;
        user=_user;
        setAllClaims=false;
        amountRequestedClousers=_amountRequestedClousers;
        encryptedKey =_encryptedKey;
        provider=_requestingProvider;
    }

    function getOwner() public constant returns(address){
        return owner;
    }

    function getUser() public constant returns(address){
        return user;
    }

    function getRequestingProvider() public constant returns(address){
        return provider;
    }

    function getEncryptedKey() public constant returns(string){
        return encryptedKey;
    }
     function getsetAllClaims() public constant returns(bool){
        return setAllClaims;
    }


    //requestedClouser

    function getRequestedClouserAmount() public constant returns(uint){
        return amountRequestedClousers;
    }

     function getRequestedClouserByIndex(uint index) public constant returns(string){
        return requestedClouser[index];
    }

      function addClouserAsRequestingProvider(string clouserContent) public ifOwner (){
        requestedClouser.push(clouserContent);
    }

    //approvedcLousers

    function getApprovedClouserAmount() public constant returns(uint){
        return amountApprovedClousers;
    }

    function getApprovedClouserByIndex(uint index) public constant returns(string){
        return approvedClouser[index];
    }

    function addToApprovedClouserAsUser(string clouserContent) public ifUser (){
        approvedClouser.push(clouserContent);
    }

    function setApprovedClouserAmount(uint amount) public ifUser (){
        amountApprovedClousers= amount;
    }


    //--suicide

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