pragma solidity ^0.4.11;

contract PermissionContract{

    address owner;
    address user;
    string claims;
    bool claimsApproved;

    function PermissionContract(address _user, string _claims) public{
        owner=msg.sender;
        user=_user;
        claims=_claims;
        claimsApproved=false;
    }

    function getClaimsApproved() public constant returns(bool){
        return claimsApproved;
    }

    function getClaims() public constant returns (string) {
        return claims;
    }

    function setAndApproveClaims(string _claims) public ifUser{
        claims=_claims;
        claimsApproved=true;
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