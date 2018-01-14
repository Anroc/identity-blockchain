pragma solidity ^0.4.11;

contract PermissionContract{

    address owner;
    address user;
    Claim [] claims;
    bool claimsApproved;

     struct Claim {
        bytes32 claimId;
        bytes32 signedClaimId;
    }

    function getClaimsApproved() public constant returns(bool){
        return claimsApproved;
    }
    function setClaimsApproved() public ifUser returns(bool){
        claimsApproved=true;
    }

    function PermissionContract(address _user, bytes32[]_requriedClaims) public{
        owner=msg.sender;
        user=_user;
        claimsApproved=false;
        for (uint8 i = 0; i < _requriedClaims.length; i++){
            bytes32 _signedClaimId;
            claims.push(Claim(_requriedClaims[i],_signedClaimId));
        }
    }

    function getClaims() public constant returns(bytes32[]){
        bytes32[] claimIds;
        for (uint8 i = 0; i < claims.length; i++){
            Claim storage c = claims[i];
            claimIds.push(c.claimId);
        }
        return claimIds;
    }

    function getSignedClaims() public constant returns(bytes32[]){
        bytes32[] claimIds;
        for (uint8 i = 0; i < claims.length; i++){
            Claim storage c = claims[i];
            claimIds.push(c.signedClaimId);
        }
        return claimIds;
    }

    modifier ifUser(){
        if(user != msg.sender){
            return;
        }
        else{
            _;
        }
    }


}