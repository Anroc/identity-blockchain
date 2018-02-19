pragma solidity ^0.4.11;

contract ClosureContract{

    address private owner;
    address private user;
    address private provider;
    string [] private requestedClosure;
    string [] private approvedClosure;
    bool private setAllClaims;
    uint private amountApprovedClosures;
    uint private amountRequestedClosures;
    string private encryptedKeyRequestedClosure;
    string private encryptedKeyApprovedClosures;

    function ClosureContract(address _user,address _requestingProvider, uint _amountRequestedClosures, string _encryptedKeyRequestedClosure) public{
        owner=msg.sender;
        user=_user;
        setAllClaims=false;
        amountRequestedClosures=_amountRequestedClosures;
        encryptedKeyRequestedClosure =_encryptedKeyRequestedClosure;
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



     function getsetAllClaims() public constant returns(bool){
        return setAllClaims;
    }


    //requestedClosure
    function getEncryptedKeyForRequestedClosure() public constant returns(string){
        return encryptedKeyRequestedClosure;
    }

    function getRequestedClosureAmount() public constant returns(uint){
        return amountRequestedClosures;
    }

     function getRequestedClosureByIndex(uint index) public constant returns(string){
        return requestedClosure[index];
    }

      function addClosureAsRequestingProvider(string ClosureContent) public ifOwner (){
        requestedClosure.push(ClosureContent);
    }

    //approvedClosures

    function getApprovedClosureAmount() public constant returns(uint){
        return amountApprovedClosures;
    }

    function getApprovedClosureByIndex(uint index) public constant returns(string){
        return approvedClosure[index];
    }

    function addToApprovedClosureAsUser(string ClosureContent) public ifUser (){
        approvedClosure.push(ClosureContent);
    }

    function setApprovedClosureAmount(uint amount) public ifUser (){
        amountApprovedClosures= amount;
    }

    function setEncryptedKeyForApprovedClosure(string _encryptedKeyApprovedClosure) public ifUser{
        encryptedKeyApprovedClosures= _encryptedKeyApprovedClosure;
    }

    function getEncryptedKeyForApprovedClosure() public constant returns(string){
        return encryptedKeyApprovedClosures;
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