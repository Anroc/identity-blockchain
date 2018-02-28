pragma solidity ^0.4.11;

contract PermissionContract{

    address private owner;
    address private user;
    string private claims;

    function PermissionContract(address _user, string _claims) public{
        owner=msg.sender;
        user=_user;
        claims=_claims;
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