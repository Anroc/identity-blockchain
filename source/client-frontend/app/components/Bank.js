/* eslint-disable semi,react/jsx-tag-spacing,jsx-quotes,space-before-blocks,space-before-function-paren,comma-dangle,no-unused-vars */
import React, { Component } from 'react';
import ExpansionPanel, {
  ExpansionPanelSummary,
  ExpansionPanelDetails,
} from 'material-ui/ExpansionPanel';
import Typography from 'material-ui/Typography';
import Input, { InputLabel } from 'material-ui/Input';
import { FormControl, FormHelperText } from 'material-ui/Form';
import Table, { TableBody, TableCell, TableHead, TableRow } from 'material-ui/Table';
import Button from 'material-ui/Button';
import { MenuItem } from 'material-ui/Menu';
import Select from 'material-ui/Select';
import TextField from 'material-ui/TextField';
import moment from 'moment'
import Snackbar from 'material-ui/Snackbar';
import IconButton from 'material-ui/IconButton';
import CloseIcon from 'material-ui-icons/Close';
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
      availableClaimsForEthAddress: [],
      snackOpen: false,
      snackMessage: '',
    };

    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.getNewMessages = this.getNewMessages.bind(this);
    this.prepareClaimOutput = this.prepareClaimOutput.bind(this);
    this.handleChangeClosureCreationClaimId = this.handleChangeClosureCreationClaimId.bind(this);
    this.handleChangeClosureCreationClaimOperation = this.handleChangeClosureCreationClaimOperation.bind(this);
    this.handleChangeClosureCreationStaticValue = this.handleChangeClosureCreationStaticValue.bind(this);
    this.handleChangeClosureCreationClaimValue = this.handleChangeClosureCreationClaimValue.bind(this);
    this.handleGetClaimsForEthAddress = this.handleGetClaimsForEthAddress.bind(this);
    this.handleChangeRequiredAttributeSelection = this.handleChangeRequiredAttributeSelection.bind(this);
    this.handleChangeOptionalAttributeSelection = this.handleChangeOptionalAttributeSelection.bind(this);
    this.switchClosureOperationLinguisticValueAndProgrammaticValue = this.switchClosureOperationLinguisticValueAndProgrammaticValue.bind(this);
    this.clearStaticValueOfNullForTable = this.clearStaticValueOfNullForTable.bind(this);
    this.prepareClosureCreationDateOutput = this.prepareClosureCreationDateOutput.bind(this);
    this.cleanFormClosures = this.cleanFormClosures.bind(this);
    this.getAllMessages = this.getAllMessages.bind(this);
    this.handleClickSnack = this.handleClickSnack.bind(this);
    this.handleCloseSnack = this.handleCloseSnack.bind(this);
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
    this.getAllMessages();
  }

  handleClickSnack(open, message){
    this.setState({
      snackOpen: open,
      snackMessage: message,
    });
  }

  handleCloseSnack(){
    this.setState({
      snackOpen: false,
      snackMessage: '',
    });
  }

  handleChange(event){
    this.setState({ ethAddress: event.target.value });
  }

  handleChangeClosureCreationClaimValue(event){
    this.setState({ closureCreationClaimValue: event.target.value });
  }

  switchClosureOperationLinguisticValueAndProgrammaticValue(val){
    // console.log(`Morphing ${val} back.`);
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
      default: return '';
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
      default:
        closureOperations = [];
        console.log('Setting ClaimOperationChoices to default');
    }
    this.setState({
      closureCreationClaimId: event.target.value,
      closureCreationClaimOperationOptions: closureOperations,
    });
  }

  handleChangeClosureCreationClaimOperation(event){
    console.log('Setting ClaimOperation to: ', event.target.value);
    this.setState({ closureCreationClaimOperation: event.target.value });
  }

  handleChangeClosureCreationStaticValue(event){
    console.log('Preparing to set new closureValue to: ', event.target.value);
    const date = moment(event.target.value);
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
  }

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
        Authorization: 'Basic YWRtaW46cGFzc3dvcmQ=',
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
        for (const userID of this.state.userIDs) {
          const getUser = {
            method: 'GET',
            headers: {
              Authorization: 'Basic YWRtaW46cGFzc3dvcmQ=',
              Accept: 'application/json',
              'Content-Type': 'application/json',
            },
            mode: 'cors',
            credentials: 'include',
          };
          console.log('Requesting user through GET: ', userID);
          request(`http://srv01.snet.tu-berlin.de:8102/users/${userID}`, getUser)
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
    // this.handleClickSnack(true, 'Users have been requested');
    // this.setState({
    //   messages: [],
    //   userIDs: [],
    //   tableData: [],
    // });
    const getMessages = {
      method: 'GET',
      headers: {
        Authorization: 'Basic YWRtaW46cGFzc3dvcmQ=',
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
        // console.log('userIDs from messages: ', this.state.userIDs);
      }).then(() => {
      // console.log(this.state.userIDs);
        // console.log('Preparing filtered userID array: ', Array.from(new Set(this.state.userIDs)));
        this.setState({
          userIDs: Array.from(new Set(this.state.userIDs)),
        });
        for (const userID of this.state.userIDs) {
          const getUser = {
            method: 'GET',
            headers: {
              Authorization: 'Basic YWRtaW46cGFzc3dvcmQ=',
              Accept: 'application/json',
              'Content-Type': 'application/json',
            },
            mode: 'cors',
            credentials: 'include',
          };
          // console.log('Requesting user through GET: ', userID);
          request(`http://srv01.snet.tu-berlin.de:8102/users/${userID}`, getUser)
            .then((json) => {
              // console.log(JSON.stringify(json));
              let newTableData = this.state.tableData;
              let pushElement = false;
              for (let i = 0; i < this.state.tableData.length; i++) {
                if (this.state.tableData[i].ethId === json.ethId) {
                  console.log(`EthAddress ${json.ethId} was successfully updated with new data.`);
                  this.state.tableData[i] = json;
                  pushElement = false;
                  break;
                } else {
                  pushElement = true;
                }
              }
              if (pushElement === true) {
                console.log('Pushed new element into tableData: ', json);
                newTableData.push(json);
              }
              if (this.state.tableData.length < 1) {
                console.log('Pushed new element into tableData: ', json);
                newTableData.push(json);
              }
              this.setState({
                tableData: newTableData,
              });
              // console.log('tableData is now: ', this.state.tableData);
            });
        }
      // this.putAllMessage();
      });
    setTimeout(this.getAllMessages, 20000);
  };

  putAllMessage(seen) {
    for (const message of this.state.messages) {
      console.log('PUT MESSAGE TO SEEN:', message);
      const getUserInformationOptions = {
        method: 'PUT',
        headers: {
          Authorization: 'Basic YWRtaW46cGFzc3dvcmQ=',
          Accept: 'application/json',
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          seen
        }),
        credentials: 'include',
      };
      console.log(`http://srv01.snet.tu-berlin.de:8102/messages/${message.id}`);
      request(`http://srv01.snet.tu-berlin.de:8102/messages/${message.id}`, getUserInformationOptions);
    }
  }

  // Submit all attributes
  handleSubmit(event){
    this.handleClickSnack(true, 'Permission has been requested');
    // alert('A name was submitted: ' + this.state.requiredAttributes.join(', '));
    event.preventDefault();
    const claimOperationMorphed = this.switchClosureOperationLinguisticValueAndProgrammaticValue(this.state.closureCreationClaimOperation);
    this.setState({
      closureRequests: [
        {
          claimID: this.state.closureCreationClaimId,
          claimOperation: claimOperationMorphed,
          staticValue: this.state.closureCreationStaticValue,
        }
      ],
    });

    const closureRequests = [
      {
        claimID: this.state.closureCreationClaimId,
        claimOperation: claimOperationMorphed,
        staticValue: this.state.closureCreationStaticValue,
      }
    ];
    let postRequest = {};
    if (this.state.closureCreationClaimId === '') {
      postRequest = {
        optionalClaims: this.state.optionalAttributes,
        providerURL: 'http://srv01.snet.tu-berlin.de:8100',
        requiredClaims: this.state.requiredAttributes,
        userEthID: this.state.ethAddress,
      };
    } else {
      postRequest = {
        closureRequests,
        optionalClaims: this.state.optionalAttributes,
        providerURL: 'http://srv01.snet.tu-berlin.de:8100',
        requiredClaims: this.state.requiredAttributes,
        userEthID: this.state.ethAddress,
      };
    }

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
    this.cleanFormClosures();
  }

  handleGetClaimsForEthAddress(event){
    // this.handleClickSnack(true, 'All available claims have been requested');
    const getMessages = {
      method: 'GET',
      headers: {
        Authorization: 'Basic YWRtaW46cGFzc3dvcmQ=',
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
      closureCreationStaticValue: {},
    });
  }

  prepareClaimOutput(c){
    // console.log('Preparing claim for returnString: ', c);
    let returnString = '';
    if (c.claimValue != null && c.id != null){
      if (c.claimValue.payload.value != null) {
        returnString += `[${c.id}(${c.claimValue.payload.value})]; `;
      } else {
        returnString += `[${c.id}(${c.claimValue.payload.timeValue})]; `;
      }
    } else {
      returnString += `[${c.id}(NULL)]; `;
    }
    return returnString;
  }

  prepareClosureOutput(c){
    // console.log('Preparing closure for returnString: ', c);
    let returnString = '';
    if (c.length > 0){
      for (let entry of c) {
        // console.log('Current closure entry is: ', entry);
        returnString +=
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
  }

  prepareClosureCreationDateOutput(c){
    // console.log('Preparing closure creation date for returnString: ', c);
    if (c.length > 0){
      for (let entry of c) {
        const creationDate = [];
        entry.payload.creationDate.forEach((number) => {
          if (Number(number) < 10) {
            creationDate.push((`0${number}`));
          } else {
            creationDate.push(number);
          }
        });
        return `[(${creationDate[3]}:${creationDate[4]}:${creationDate[5]} o'clock)
      (${creationDate[2]}.${creationDate[1]}.${creationDate[0]})]`;
      }
    }
    return '';
  }

  clearStaticValueOfNullForTable(staticValue){
    if (!staticValue.value){
      const staticValueTimeValue = [];
      for (let number of staticValue.timeValue) {
        // console.log('Iterating over: ' + number + ' is smaller than 10: ' + (Number(number) < 10));
        if (Number(number) < 10) {
          staticValueTimeValue.push((`0${number}`));
        } else {
          staticValueTimeValue.push(number);
        }
      }
      return `[(${staticValueTimeValue[3]}:${staticValueTimeValue[4]} o'clock)
      (${staticValueTimeValue[2]}.${staticValueTimeValue[1]}.${staticValueTimeValue[0]})]`;
    }
    return staticValue.value;
  }

  render(){
    return (
      <article
        style={{
          max: '100%',
          width: '100%',
        }}
      >
        <Snackbar
          anchorOrigin={{
            vertical: 'top',
            horizontal: 'center',
          }}
          open={this.state.snackOpen}
          autoHideDuration={6000}
          onClose={this.handleCloseSnack}
          message={<span id="message-id">{this.state.snackMessage}</span>}
          action={[
            <IconButton
              key="close"
              aria-label="Close"
              color="inherit"
              onClick={this.handleCloseSnack}
            >
              <CloseIcon />
            </IconButton>,
          ]}
        />
        <section>
          <ExpansionPanel>
            <ExpansionPanelSummary>
              <Typography>Create new Permission/Closure request</Typography>
            </ExpansionPanelSummary>
            <ExpansionPanelDetails
              style={{ flexDirection: 'row', flexWrap: 'wrap', flexFlow: 'column' }}
            >
              <div>
                <FormControl
                  aria-describedby="ethAddress-text"
                  style={{ marginBottom: '15px', max: '100%', width: '100%' }}
                >
                  <InputLabel htmlFor="ethAddress-helper">Ethereum Address</InputLabel>
                  <Input id="ethAddress"
                    value={this.state.ethAddress}
                    onChange={this.handleChange}
                    onBlur={this.handleGetClaimsForEthAddress}
                  />
                </FormControl>
              </div>
              <div>
                <ExpansionPanel>
                  <ExpansionPanelSummary>
                    <Typography>Create a new Permission request</Typography>
                  </ExpansionPanelSummary>
                  <ExpansionPanelDetails
                    style={{ display: 'flex', flexWrap: 'wrap' }}
                  >
                    <FormControl
                      style={{ marginBottom: '15px', max: '100%', width: '100%' }}
                    >
                      <InputLabel htmlFor="select-multiple-required-claims">Required Claims</InputLabel>
                      <Select
                        multiple
                        value={this.state.requiredAttributes}
                        onChange={this.handleChangeRequiredAttributeSelection}
                        input={<Input id="select-multiple" />}
                      >
                        { this.state.availableClaimsForEthAddress.map((c) => (
                          this.state.optionalAttributes.includes(c.claimID)
                            ? null
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
                        style={{ marginBottom: '15px', max: '100%', width: '100%' }}
                        >
                        <InputLabel htmlFor="select-multiple-optional-claims">Optional Claims</InputLabel>
                        <Select
                          multiple
                          value={this.state.optionalAttributes}
                          onChange={this.handleChangeOptionalAttributeSelection}
                          input={<Input id="select-multiple" />}
                        >
                          { this.state.availableClaimsForEthAddress.map((c) => (
                            this.state.requiredAttributes.includes(c.claimID) > 0 || Object.entries(this.state.optionalAttributes.includes(c.claimID)).length > 1
                            ? null
                              : <MenuItem
                                key={c.claimID}
                                value={c.claimID}
                              >
                                {c.claimID}
                              </MenuItem>
                          ))}
                        </Select>
                      </FormControl>
                      : null
                    }
                  </ExpansionPanelDetails>
                </ExpansionPanel>
              </div>
              <div>
                <ExpansionPanel>
                  <ExpansionPanelSummary>
                    <Typography>Create a new Closure request</Typography>
                  </ExpansionPanelSummary>
                  <ExpansionPanelDetails
                    style={{ display: 'flex', flexWrap: 'wrap' }}
                  >
                    <FormControl
                      style={{ marginBottom: '15px', max: '100%', width: '100%' }}
                    >
                      <InputLabel htmlFor="claimId">Claim-ID</InputLabel>
                      <Select
                        value={this.state.closureCreationClaimId}
                        onChange={this.handleChangeClosureCreationClaimId}
                      >
                        {this.state.availableClaimsForEthAddress.map((c) => (
                          <MenuItem
                            value={c.claimID}
                            key={c.claimID}
                          >{c.claimID}
                          </MenuItem>
                        ))}
                      </Select>
                    </FormControl>
                    <FormControl
                      style={{ marginBottom: '15px', max: '100%', width: '100%' }}
                    >
                      <InputLabel htmlFor="claimOperation">Claim-Operation</InputLabel>
                      <Select
                        value={this.state.closureCreationClaimOperation}
                        onChange={this.handleChangeClosureCreationClaimOperation}
                      >
                        {this.state.availableClaimsForEthAddress.map((c) => (
                          c.claimID === this.state.closureCreationClaimId
                            ? c.claimOperations.map((o) => (
                              <MenuItem
                                key={this.state.closureOperationDescriptions[o]}
                                value={this.state.closureOperationDescriptions[o]}
                              >
                                {this.state.closureOperationDescriptions[o]}
                              </MenuItem>
                            )) : null
                        ))}
                      </Select>
                    </FormControl>
                    { this.state.availableClaimsForEthAddress.map((c) => (
                      c.claimID === this.state.closureCreationClaimId
                        ? c.claimType === 'DATE'
                        ? <FormControl>
                          <TextField
                            id="date"
                            type="date"
                            label="date"
                            key={'closureDateValueInput'}
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
                          <Input
                            id="closureCreationClaimValue"
                            value={this.state.closureCreationClaimValue}
                            onChange={this.handleChangeClosureCreationStaticValue}
                          />
                        </FormControl>
                        : null
                    ))}
                  </ExpansionPanelDetails>
                </ExpansionPanel>
              </div>
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
              <Typography>Granted Permissions/Closures</Typography>
            </ExpansionPanelSummary>
            <ExpansionPanelDetails>
              <div>
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
                          }}
                        >{n.claims.map((c) => (
                          this.prepareClaimOutput(c)
                          )
                        )}</TableCell>
                        <TableCell
                          style={{
                            whiteSpace: 'normal',
                            wordWrap: 'break-word'
                          }}
                        >
                          { n.claims.map((c) => (
                            this.prepareClosureOutput(c.signedClosures)
                          ))}
                        </TableCell>
                        <TableCell
                          style={{
                            whiteSpace: 'normal',
                            wordWrap: 'break-word'
                          }}
                        >
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
