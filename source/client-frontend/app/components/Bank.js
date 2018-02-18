/* eslint-disable semi,react/jsx-tag-spacing,jsx-quotes,space-before-blocks,space-before-function-paren,comma-dangle,no-unused-vars */
import React, { Component } from 'react';
import ExpansionPanel, {
  ExpansionPanelSummary,
  ExpansionPanelDetails,
} from 'material-ui/ExpansionPanel';
import Typography from 'material-ui/Typography';
import Divider from 'material-ui/Divider';
import Input, { InputLabel } from 'material-ui/Input';
import { FormControl, FormHelperText } from 'material-ui/Form';
import Table, { TableBody, TableCell, TableHead, TableRow } from 'material-ui/Table';
import Button from 'material-ui/Button';
import AddIcon from 'material-ui-icons/Add';
import IconButton from 'material-ui/IconButton';
import DeleteIcon from 'material-ui-icons/Delete';
import RefreshIcon from 'material-ui-icons/Refresh';
import Paper from 'material-ui/Paper';
import { MenuItem } from 'material-ui/Menu';
import Select from 'material-ui/Select';
import TextField from 'material-ui/TextField';
import moment from 'moment'
import { ListItemText } from 'material-ui/List';
import request from '../auth/request';

class Bank extends Component{
  constructor(){
    super();
    this.state = {
      name: '',
      closureRequests: [],
      requiredAttributes: [],
      optionalAttributes: [],
      ethAddress: '',
      selected: [1],
      claimChoices: {
        GIVEN_NAME: 'STRING',
        FAMILY_NAME: 'STRING',
        BIRTHDAY: 'DATE',
        MAIN_RESIDENCE_COUNTRY: 'STRING',
        MAIN_RESIDENCE_STREET: 'STRING',
        MAIN_RESIDENCE_STREET_NUMBER: 'STRING',
        MAIN_RESIDENCE_ZIP_CODE: 'NUMBER',
        MAIN_RESIDENCE_CITY: 'STRING',
      },
      messages: [],
      grantedPermissions: [],
      userIDs: [],
      tableData: [],
      closureTypes: {
        NUMBER: ['EQ', 'NEQ', 'GT', 'GE', 'LT', 'LE'],
        STRING: ['EQ', 'NEQ'],
        OBJECT: [],
        DATE: ['EQ', 'NEQ', 'GT', 'GE', 'LT', 'LE'],
        BOOLEAN: ['EQ', 'NEQ'],
      },
      closureOperationDescriptions: {
        EQ: 'equals',
        NEQ: 'does not equal',
        GT: 'greater than',
        GE: 'greater or equals',
        LT: 'less than',
        LE: 'less or equals',
      },
      closureCreationEthAddress: '',
      closureCreationClaimId: '',
      closureCreationClaimOperation: '',
      closureCreationClaimValue: '',
      closureCreationClaimOperationOptions: [],
      closureCreationStaticValue: {
        timeValue: '',
        value: '',
      },
      availableClaimsForClosuresForEthAddress: [],
      availableClaimsForEthAddress: [],
    };

    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.getNewMessages = this.getNewMessages.bind(this);
    this.getAllMessages = this.getAllMessages.bind(this);
    this.prepareClaimOutput = this.prepareClaimOutput.bind(this);
    this.handleChangeClosureCreationClaimId = this.handleChangeClosureCreationClaimId.bind(this);
    this.handleChangeClosureCreationClaimOperation = this.handleChangeClosureCreationClaimOperation.bind(this);
    this.handleChangeClosureCreationStaticValue = this.handleChangeClosureCreationStaticValue.bind(this);
    this.handleChangeClosureCreationClaimValue = this.handleChangeClosureCreationClaimValue.bind(this);
    this.handleChangeClosureCreationEthAddress = this.handleChangeClosureCreationEthAddress.bind(this);
    this.handleSubmitClosure = this.handleSubmitClosure.bind(this);
    this.handleGetClosuresForClaimsForEthAddress = this.handleGetClosuresForClaimsForEthAddress.bind(this);
    this.handleGetClaimsForEthAddress = this.handleGetClaimsForEthAddress.bind(this);
    this.handleChangeRequiredAttributeSelection = this.handleChangeRequiredAttributeSelection.bind(this);
    this.handleChangeOptionalAttributeSelection = this.handleChangeOptionalAttributeSelection.bind(this);
    this.switchClosureOperationLinguisticValueAndProgrammaticValue = this.switchClosureOperationLinguisticValueAndProgrammaticValue.bind(this);
    this.clearStaticValueOfNullForTable = this.clearStaticValueOfNullForTable.bind(this);
    this.prepareClosureCreationDateOutput = this.prepareClosureCreationDateOutput.bind(this);
    this.cleanFormClosures = this.cleanFormClosures.bind(this);
  }

  componentDidMount(){
    const postRequest = {
      password: 'string',
    };
    console.log('Logging in with password: ', JSON.stringify(postRequest));
    const getUserInformationOptions = {
      method: 'POST',
      headers: {
        Accept: 'application/json',
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(postRequest),
      mode: 'cors',
      credentials: 'include',
    };
    // console.log(JSON.stringify(getUserInformationOptions));
    request('http://srv01.snet.tu-berlin.de:8102/account/login', getUserInformationOptions);
  }

  handleChange(event){
    this.setState({ ethAddress: event.target.value });
  }

  handleChangeClosureCreationEthAddress(event){
    this.setState({ closureCreationEthAddress: event.target.value });
  }

  handleChangeClosureCreationClaimValue(event){
    this.setState({ closureCreationClaimValue: event.target.value });
  }

  switchClosureOperationLinguisticValueAndProgrammaticValue(val){
    console.log('Morphing ' + val + ' back.');
    switch (val){
      case 'EQ': return 'equals';
      case 'NEQ': return 'does not equal';
      case 'GT': return 'greater than';
      case 'GE': return 'greater or equals';
      case 'LT': return 'less than';
      case 'LE': return 'less or equals';
      case 'equals': return 'EQ';
      case 'does not equal': return 'NEQ';
      case 'greater than': return 'GT';
      case 'greater or equals': return 'GE';
      case 'less than': return 'LT';
      case 'less or equals': return 'LE';
    }
  }

  handleChangeClosureCreationClaimId(event){
    console.log('ClaimID to request: ', event.target.value);
    let closureOperations = [];
    switch (event.target.value){
      case 'NUMBER':
        closureOperations = ['EQ', 'NEQ', 'GT', 'GE', 'LT', 'LE'];
        console.log('Setting ClaimOperationsChoices to: ', closureOperations);
        break;
      case 'STRING':
        closureOperations = ['EQ', 'NEQ'];
        console.log('Setting ClaimOperationsChoices to: ', closureOperations);
        break;
      case 'OBJECT':
        closureOperations = [];
        console.log('Setting ClaimOperationsChoices to: ', closureOperations);
        break;
      case 'DATE':
        closureOperations = ['EQ', 'NEQ', 'GT', 'GE', 'LT', 'LE'];
        console.log('Setting ClaimOperationsChoices to: ', closureOperations);
        break;
      case 'BOOLEAN':
        closureOperations = ['EQ', 'NEQ'];
        console.log('Setting ClaimOperationsChoices to: ', closureOperations);
        break;
    }
    this.setState({
      closureCreationClaimId: event.target.value,
      closureCreationClaimOperationOptions: closureOperations,
    });
  };

  handleChangeClosureCreationClaimOperation(event){
    console.log('Setting ClaimOperation to: ', event.target.value);
    this.setState({closureCreationClaimOperation: event.target.value});
  };

  handleChangeClosureCreationStaticValue(event){
    console.log('Preparing to set new closureValue to: ', event.target.value);
    let date = moment(event.target.value);
    if (date.isValid()){
      const splitDate = event.target.value.split('-');
      this.setState({
        closureCreationClaimValue: event.target.value,
        closureCreationStaticValue: {
          timeValue: [Number(splitDate[0]), Number(splitDate[1]), Number(splitDate[2]), 0, 0, 0],
        },
      });
    } else {
      this.setState({
        closureCreationClaimValue: event.target.value,
        closureCreationStaticValue: {
          value: event.target.value,
        },
      });
    }
  };

  handleChangeRequiredAttributeSelection(event){
    this.setState({ requiredAttributes: event.target.value });
  }

  handleChangeOptionalAttributeSelection(event){
    this.setState({ optionalAttributes: event.target.value });
  }

  // Get new information about granted requests etc.
  getNewMessages(){
    const getMessages = {
      method: 'GET',
      headers: {
        Authorization: 'Basic YWRtaW46cGVuaXNwdW1wZQ==',
        Accept: 'application/json',
        'Content-Type': 'application/json',
      },
      mode: 'cors',
      credentials: 'include',
    };

    request('http://srv01.snet.tu-berlin.de:8102/messages?includeSeen=false', getMessages)
      .then((json) => {
        // console.log(JSON.stringify(json));
        const tmp = json.map((message) => message.userId);
        // console.log('json:', json);
        // console.log('UserIDs: ', tmp);
        this.setState({
          messages: json,
          userIDs: tmp,
        });
        console.log('userIDs from messages: ', this.state.userIDs);
      }).then(() => {
        // console.log(this.state.userIDs);
        console.log('Preparing filtered userID array: ', Array.from(new Set(this.state.userIDs)));
        this.setState({
          userIDs: Array.from(new Set(this.state.userIDs)),
        });
        for (let user of this.state.userIDs) {
          const getUser = {
            method: 'GET',
            headers: {
              Authorization: 'Basic YWRtaW46cGVuaXNwdW1wZQ==',
              Accept: 'application/json',
              'Content-Type': 'application/json',
            },
            mode: 'cors',
            credentials: 'include',
          };
          console.log('Requesting user through GET: ', user);
          request('http://srv01.snet.tu-berlin.de:8102/users/' + user, getUser)
            .then((json) => {
              // console.log(JSON.stringify(json));
              const newTableData = this.state.tableData;
              if (!newTableData.includes(json)) {
                console.log('Adding new data to tableData: ', json);
                newTableData.push(json);
              }
              this.setState({
                tableData: newTableData,
              });
              console.log('tableData is now: ', this.state.tableData);
            });
        }
        // this.putAllMessage();
      });
  }

  getAllMessages(){
    this.setState({
      messages: [],
      userIDs: [],
      tableData: [],
    });
    const getMessages = {
      method: 'GET',
      headers: {
        Authorization: 'Basic YWRtaW46cGVuaXNwdW1wZQ==',
        Accept: 'application/json',
        'Content-Type': 'application/json',
      },
      mode: 'cors',
      credentials: 'include',
    };
    request('http://srv01.snet.tu-berlin.de:8102/messages?includeSeen=true', getMessages)
      .then((json) => {
        // console.log(JSON.stringify(json));
        const tmp = json.map((message) => message.userId);
        // console.log('json:', json);
        // console.log('UserIDs: ', tmp);
        this.setState({
          messages: json,
          userIDs: tmp,
        });
        console.log('userIDs from messages: ', this.state.userIDs);
      }).then(() => {
      // console.log(this.state.userIDs);
        console.log('Preparing filtered userID array: ', Array.from(new Set(this.state.userIDs)));
        this.setState({
          userIDs: Array.from(new Set(this.state.userIDs)),
        });
        for (let user of this.state.userIDs) {
          const getUser = {
            method: 'GET',
            headers: {
              Authorization: 'Basic YWRtaW46cGVuaXNwdW1wZQ==',
              Accept: 'application/json',
              'Content-Type': 'application/json',
            },
            mode: 'cors',
            credentials: 'include',
          };
          console.log('Requesting user through GET: ', user);
          request('http://srv01.snet.tu-berlin.de:8102/users/' + user, getUser)
            .then((json) => {
              // console.log(JSON.stringify(json));
              const newTableData = this.state.tableData;
              if (!newTableData.includes(json)) {
                console.log('Adding new data to tableData: ', json);
                newTableData.push(json);
              }
              this.setState({
                tableData: newTableData,
              });
              console.log('tableData is now: ', this.state.tableData);
            });
        }
      // this.putAllMessage();
      });
  };

  putAllMessage(seen) {
    for (let message of this.state.messages) {
      console.log('PUT MESSAGE TO SEEN:', message);
      const getUserInformationOptions = {
        method: 'PUT',
        headers: {
          Authorization: 'Basic YWRtaW46cGVuaXNwdW1wZQ==',
          Accept: 'application/json',
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          seen: seen
        }),
        credentials: 'include',
      };
      console.log(`http://srv01.snet.tu-berlin.de:8102/messages/${message.id}`);
      request(`http://srv01.snet.tu-berlin.de:8102/messages/${message.id}`, getUserInformationOptions);
    }
  }

  // Submit all attributes
  handleSubmit(event){
    // alert('A name was submitted: ' + this.state.requiredAttributes.join(', '));
    event.preventDefault();

    const postRequest = {
      closureRequests: this.state.closureRequests,
      optionalClaims: this.state.optionalAttributes,
      providerURL: 'http://srv01.snet.tu-berlin.de:8100',
      requiredClaims: this.state.requiredAttributes,
      userEthID: this.state.ethAddress,
    };
    console.log('Preparing following body for POST: ', JSON.stringify(postRequest));
    const getUserInformationOptions = {
      method: 'POST',
      headers: {
        Accept: 'application/json',
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(postRequest),
      mode: 'cors',
      credentials: 'include',
    };
    // console.log(JSON.stringify(getUserInformationOptions));
    request('http://srv01.snet.tu-berlin.de:8102/permissions', getUserInformationOptions);
      // .then((json) => {
      //  this.setState({
      //    claims: json,
      //  });
      // }).catch((error) => {
      //  console.log(error);
      // });

    this.cleanFormClaims();
  }

  handleSubmitClosure(event){
    // alert('A name was submitted: ' + this.state.requiredAttributes.join(', '));
    event.preventDefault();
    const claimOperationMorphed = this.switchClosureOperationLinguisticValueAndProgrammaticValue(this.state.closureCreationClaimOperation);
    console.log('Claim operator was morphed back to: ', claimOperationMorphed);
    this.setState({
      closureRequests: [
        {
          claimID: this.state.closureCreationClaimId,
          claimOperation: claimOperationMorphed,
          staticValue: this.state.closureCreationStaticValue,
        }
      ],
    });

    console.log('Closure request: claim-id' + this.state.closureCreationClaimId + ', claimOperation ' + claimOperationMorphed + ', value ' + this.state.closureCreationStaticValue);
    const closureRequests = [
      {
        claimID: this.state.closureCreationClaimId,
        claimOperation: claimOperationMorphed,
        staticValue: this.state.closureCreationStaticValue,
      }
    ];
    console.log('Closure request is: ', closureRequests);
    const postRequest = {
      closureRequests: closureRequests,
      optionalClaims: [],
      providerURL: 'http://srv01.snet.tu-berlin.de:8100',
      requiredClaims: [],
      userEthID: this.state.closureCreationEthAddress,
    };
    console.log('Preparing following body for POST: ', JSON.stringify(postRequest));
    const getUserInformationOptions = {
      method: 'POST',
      headers: {
        Accept: 'application/json',
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(postRequest),
      mode: 'cors',
      credentials: 'include',
    };
    // console.log(JSON.stringify(getUserInformationOptions));
    request('http://srv01.snet.tu-berlin.de:8102/permissions', getUserInformationOptions);
    // .then((json) => {
    //  this.setState({
    //    claims: json,
    //  });
    // }).catch((error) => {
    //  console.log(error);
    // });
    this.cleanFormClosures();
  }

  handleGetClosuresForClaimsForEthAddress(event){
    const getMessages = {
      method: 'GET',
      headers: {
        Authorization: 'Basic YWRtaW46cGVuaXNwdW1wZQ==',
        Accept: 'application/json',
        'Content-Type': 'application/json',
      },
      mode: 'cors',
      credentials: 'include',
    };

    request(`http://srv01.snet.tu-berlin.de:8100/users/ethID/${this.state.closureCreationEthAddress}/claimIDs`, getMessages)
      .then((json) => {
        // console.log(JSON.stringify(json));
        this.setState({
          availableClaimsForClosuresForEthAddress: json,
        });
        console.log('Available closures for claims for given eth address: ', this.state.availableClaimsForClosuresForEthAddress);
      })
  }

  handleGetClaimsForEthAddress(event){
    const getMessages = {
      method: 'GET',
      headers: {
        Authorization: 'Basic YWRtaW46cGVuaXNwdW1wZQ==',
        Accept: 'application/json',
        'Content-Type': 'application/json',
      },
      mode: 'cors',
      credentials: 'include',
    };

    request(`http://srv01.snet.tu-berlin.de:8100/users/ethID/${this.state.ethAddress}/claimIDs`, getMessages)
      .then((json) => {
        // console.log(JSON.stringify(json));
        this.setState({
          availableClaimsForEthAddress: json,
        });
        console.log('Available claims for given eth address: ', this.state.availableClaimsForEthAddress);
      })
  }

  cleanFormClaims(){
    // console.log('test', this.state);
    this.setState({
      requiredAttributes: [],
      optionalAttributes: [],
    });
  }

  cleanFormClosures(){
    // console.log('test', this.state);
    this.setState({
      closureCreationClaimId: '',
      closureCreationClaimOperation: '',
      closureCreationClaimValue: '',
    });
  }

  prepareClaimOutput(c){
    console.log('Preparing claim for returnString: ', c);
    let returnString = '';
    if (c.claimValue != null && c.id != null){
      if (c.claimValue.payload.value != null) {
        returnString += '[' + c.id + '(' + c.claimValue.payload.value + ')' + ']; ';
      } else {
        returnString += '[' + c.id + '(' + c.claimValue.payload.timeValue + ')' + ']; ';
      }
    } else {
      returnString += '[' + c.id + '(NULL)' + ']; ';
    }
    return returnString;
  };

  prepareClosureOutput(c){
    // console.log('Preparing closure for returnString: ', c);
    let returnString = '';
    if (c.length > 0){
      for (let entry of c) {
        // console.log('Current closure entry is: ', entry);
        returnString = returnString +
          '[' + entry.payload.claimID + ' ' +
          this.switchClosureOperationLinguisticValueAndProgrammaticValue(entry.payload.claimOperation) +
          ' ' + this.clearStaticValueOfNullForTable(entry.payload.staticValue) + ': ' +
          entry.payload.expressionResult +
          ']; ';
      }
    } else {
      returnString = '';
    }
    return returnString
  };

  prepareClosureCreationDateOutput(c){
    console.log('Preparing closure creation date for returnString: ', c);
    let returnString = '';
    if (c.length > 0){
      for (let entry of c) {
        let creationDateNoSingleChar = [];
        for (let number of entry.payload.creationDate) {
          // console.log('Iterating over: ' + number + ' is smaller than 10: ' + (Number(number) < 10) );
          if (Number(number) < 10) {
            creationDateNoSingleChar.push(('0' + number));
          } else {
            creationDateNoSingleChar.push(number);
          }
        }
        // console.log('Current closure entry is: ', entry);
        returnString = returnString +
          '[(' + creationDateNoSingleChar[3] + ':' +
          creationDateNoSingleChar[4] + ':' +
          creationDateNoSingleChar[5] + ' Uhr) (' +
          creationDateNoSingleChar[2] + '.' +
          creationDateNoSingleChar[1] + '.' +
          creationDateNoSingleChar[0] +
        ')]; ';
      }
    } else {
      returnString = '';
    }
    return returnString
  };

  clearStaticValueOfNullForTable(staticValue){
    if (!staticValue.value){
      let staticValueTimeValueNoSingleChar = [];
      for (let number of staticValue.timeValue) {
        // console.log('Iterating over: ' + number + ' is smaller than 10: ' + (Number(number) < 10));
        if (Number(number) < 10) {
          staticValueTimeValueNoSingleChar.push(('0' + number));
        } else {
          staticValueTimeValueNoSingleChar.push(number);
        }
      }
      return '[(' + staticValueTimeValueNoSingleChar[3] + ':' +
        staticValueTimeValueNoSingleChar[4] + ' Uhr) (' +
        staticValueTimeValueNoSingleChar[2] + '.' +
        staticValueTimeValueNoSingleChar[1] + '.' +
        staticValueTimeValueNoSingleChar[0] + ')]';
    } else {
      return staticValue.value;
    }
  };

  handleMarkMessageAsSeen(c){

  };

  render(){
    return (
      <article style={{
        max: '100%',
        width: '100%',
      }}>
        <section>
          <ExpansionPanel>
            <ExpansionPanelSummary>
              <Typography>Create new permission request</Typography>
            </ExpansionPanelSummary>
            <ExpansionPanelDetails
              style={{ display: 'flex', flexWrap: 'wrap' }}>
              <FormControl
                aria-describedby="ethAddress-text"
                style={{ marginBottom: '15px', minWidth: '75%' }}
              >
                <InputLabel htmlFor="ethAddress-helper">Ethereum Address</InputLabel>
                <Input id="ethAddress" value={this.state.ethAddress} onChange={this.handleChange}/>
              </FormControl>
              <Button
                raised
                onClick={this.handleGetClaimsForEthAddress}
                style={{ marginLeft: '15px', marginBottom: '100px' }}
              >Get Claim-IDs</Button>
              <FormControl
                style={{ marginBottom: '15px', max: '100%', width: '100%' }}>
                <InputLabel htmlFor="select-multiple-required-claims">Required Claims</InputLabel>
                <Select
                  multiple
                  value={this.state.requiredAttributes}
                  onChange={this.handleChangeRequiredAttributeSelection}
                  input={<Input id="select-multiple" />}
                >
                  { this.state.availableClaimsForEthAddress.map((c) => (
                    this.state.optionalAttributes.length > 0
                    ? this.state.optionalAttributes.map((oa) => (
                      c.claimID === oa
                      ? null
                      : <MenuItem
                        key={c.claimID}
                        value={c.claimID}
                        >
                        {c.claimID}
                      </MenuItem>
                    ))
                    : <MenuItem
                      key={c.claimID}
                      value={c.claimID}
                      >
                      {c.claimID}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
              { this.state.requiredAttributes.length > 0
              ? <FormControl
                style={{ marginBottom: '15px', max: '100%', width: '100%' }}>
                <InputLabel htmlFor="select-multiple-optional-claims">Optional Claims</InputLabel>
                <Select
                  multiple
                  value={this.state.optionalAttributes}
                  onChange={this.handleChangeOptionalAttributeSelection}
                  input={<Input id="select-multiple" />}
                >
                  { this.state.availableClaimsForEthAddress.map((c) => (
                    this.state.requiredAttributes.map((ra) => (
                      c.claimID === ra
                        ? null
                        : <MenuItem
                          key={c.claimID}
                          value={c.claimID}
                        >
                          {c.claimID}
                        </MenuItem>
                    ))
                  ))}
                </Select>
              </FormControl>
              : null
              }
              <Button
                raised
                onClick={this.handleSubmit}
                style={{ marginTop: '15px', marginLeft: '15px', marginBottom: '15px' }}
              >Submit</Button>
            </ExpansionPanelDetails>
          </ExpansionPanel>
        </section>
        <section>
          <ExpansionPanel>
            <ExpansionPanelSummary>
              <Typography>Create a new Closure request</Typography>
            </ExpansionPanelSummary>
            <ExpansionPanelDetails
              style={{ display: 'flex', flexWrap: 'wrap' }}>
              <FormControl
                aria-describedby="closureCreationEthAddress-text"
                style={{ marginBottom: '15px', minWidth: '75%' }}
              >
                <InputLabel htmlFor="closureCreationEthAddress-helper">Ethereum Address</InputLabel>
                <Input id="closureCreationEthAddress" value={this.state.closureCreationEthAddress} onChange={this.handleChangeClosureCreationEthAddress}/>
              </FormControl>
              <Button
                raised
                onClick={this.handleGetClosuresForClaimsForEthAddress}
                style={{ marginLeft: '15px', marginBottom: '100px' }}
              >Get Claim-IDs</Button>
              <FormControl
                style={{ marginBottom: '15px', max: '100%', width: '100%' }}>
                <InputLabel htmlFor="claimId">Claim-ID</InputLabel>
                <Select
                  value={this.state.closureCreationClaimId}
                  onChange={this.handleChangeClosureCreationClaimId}
                >
                  {this.state.availableClaimsForClosuresForEthAddress.map((c) => (
                    <MenuItem value={c.claimID}>{c.claimID}</MenuItem>
                  ))}
                </Select>
              </FormControl>
              <FormControl
                style={{ marginBottom: '15px', max: '100%', width: '100%' }}>
                <InputLabel htmlFor="claimOperation">Claim-Operation</InputLabel>
                <Select
                  value={this.state.closureCreationClaimOperation}
                  onChange={this.handleChangeClosureCreationClaimOperation}
                >
                  {this.state.availableClaimsForClosuresForEthAddress.map((c) => (
                    c.claimID === this.state.closureCreationClaimId
                      ? c.claimOperations.map((o) => (
                        <MenuItem value={this.state.closureOperationDescriptions[o]}>{this.state.closureOperationDescriptions[o]}</MenuItem>
                    )) : null
                  ))}
                </Select>
              </FormControl>
              { this.state.availableClaimsForClosuresForEthAddress.map((c) => (
                c.claimID === this.state.closureCreationClaimId
                ? c.claimType === 'DATE'
                  ? <FormControl>
                    <TextField
                      id="date"
                      type="date"
                      label="date"
                      value={this.state.closureCreationClaimValue}
                      onChange={this.handleChangeClosureCreationStaticValue}
                      InputLabelProps={{
                        shrink: true,
                      }}
                    />
                  </FormControl>
                  : <FormControl
                    aria-describedby="closureCreationClaimValue-text"
                    style={{ marginBottom: '15px', max: '100%', width: '100%' }}
                  >
                    <InputLabel htmlFor="closureCreationClaimValue-helper">Value</InputLabel>
                    <Input id="closureCreationClaimValue"
                      value={this.state.closureCreationClaimValue}
                      onChange={this.handleChangeClosureCreationStaticValue}/>
                  </FormControl>
                : null
              ))}
              <Button
                raised
                onClick={this.handleSubmitClosure}
                style={{ marginTop: '15px', marginLeft: '15px', marginBottom: '15px' }}
              >Submit</Button>
            </ExpansionPanelDetails>
          </ExpansionPanel>
        </section>
        <section>
          <ExpansionPanel>
            <ExpansionPanelSummary>
              <Typography>Granted Permissions/Closures</Typography>
            </ExpansionPanelSummary>
            <ExpansionPanelDetails>
              <div>
                <Button
                  raised
                  style={{ marginTop: '15px', marginBottom: '25px' }}
                  onClick={this.getAllMessages}
                >
                  Request Users
                </Button>
                <Table>
                  <TableHead>
                    <TableRow>
                      <TableCell>ethID</TableCell>
                      <TableCell>Claims</TableCell>
                      <TableCell>Signed closures</TableCell>
                      <TableCell>Closure creation Date</TableCell>
                    </TableRow>
                  </TableHead>
                  <TableBody>
                    {this.state.tableData.map((n) => (
                      <TableRow key={n.ethId}>
                        <TableCell>{n.ethId}</TableCell>
                        <TableCell
                          style={{
                            whiteSpace: 'normal',
                            wordWrap: 'break-word'
                          }}>{n.claims.map((c) => (
                          this.prepareClaimOutput(c)
                          )
                        )}</TableCell>
                        <TableCell
                          style={{
                            whiteSpace: 'normal',
                            wordWrap: 'break-word'
                          }}>
                          { n.claims.map((c) => (
                            this.prepareClosureOutput(c.signedClosures)
                          ))}
                        </TableCell>
                        <TableCell
                          style={{
                            whiteSpace: 'normal',
                            wordWrap: 'break-word'
                          }}>
                          { n.claims.map((c) => (
                            this.prepareClosureCreationDateOutput(c.signedClosures)
                          ))}
                        </TableCell>
                      </TableRow>
                      )
                    )}
                  </TableBody>
                </Table>
              </div>
            </ExpansionPanelDetails>
          </ExpansionPanel>
        </section>
      </article>
    );
  }
}

export default Bank;
