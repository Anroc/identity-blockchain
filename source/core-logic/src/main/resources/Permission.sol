pragma solidity ^0.4.11;

contract PermissionContract{

    address private owner;
    address private user;
    address private clouserContract;
    string private claims;

    function PermissionContract(address _user, string _claims, address _clouserContract) public{
        owner=msg.sender;
        user=_user;
        claims=_claims;
        clouserContract=_clouserContract;
    }

    function getClouserContractAddress() public constant returns (address){
        return clouserContract;
    }

    function getClaims() public constant returns (string) {
        return claims;
    }

    function setAndApproveClaims(string _claims) public ifUser{
        claims=_claims;
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