pragma solidity ^0.4.11;

contract FirstContract{

    address owner;
    address gov;
    bool approval;

    function FirstContract(){
        owner=msg.sender;
        gov=0x9A06cfA07F6D65d113ac9fcd1326355ae6DB1083;
        approval=false;

    }

    modifier ifOwner(){
        if(owner != msg.sender){
            throw;
        }
        else{
            _;
        }
    }

    modifier ifGov(){
        if(gov != msg.sender){
            throw;
        }
        else{
            _;
        }
    }

    function getApproval() constant returns(bool){
        return approval;
    }

    function setApproval(bool _approval) ifGov returns(bool){
        approval = _approval;
        return approval;
    }
}