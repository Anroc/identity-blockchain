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
      data: [
        createData('eth_id', 'GIVEN_NAME', 'SELECT GIVEN_NAME FROM USERS', 'PENDING'),
        createData('eth_id_two', 'FAMILY_NAME', 'SELECT FAMILY_NAME FROM USERS', 'PENDING'),
      ],
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

    let id = 0;

    function createData(ethID, RequestedAttributes, GrantedQuery, Status){
      id += 1;
      return { ethID, RequestedAttributes, GrantedQuery, Status };
    }

    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.addClickRequiredAttributes = this.addClickRequiredAttributes.bind(this);
    this.addClickOptionalAttributes = this.addClickOptionalAttributes.bind(this);
    this.getNewMessages = this.getNewMessages.bind(this);
    this.prepareClaimOutput = this.prepareClaimOutput.bind(this);
    this.handleChangeClosureCreationClaimId = this.handleChangeClosureCreationClaimId.bind(this);
    this.handleChangeClosureCreationClaimOperation = this.handleChangeClosureCreationClaimOperation.bind(this);
    this.handleChangeClosureCreationStaticValue = this.handleChangeClosureCreationStaticValue.bind(this);
    this.handleChangeClosureCreationClaimValue = this.handleChangeClosureCreationClaimValue.bind(this);
    this.handleChangeClosureCreationEthAddress = this.handleChangeClosureCreationEthAddress.bind(this);
    this.handleSubmitClosure = this.handleSubmitClosure.bind(this);
    this.handleGetClosuresForClaimsForEthAddress = this.handleGetClosuresForClaimsForEthAddress.bind(this);
    this.handleGetClaimsForEthAddress = this.handleGetClaimsForEthAddress.bind(this);
  }

  componentDidMount(){
  }

  handleChange(event){
    this.setState({ ethAddress: event.target.value });
  }

  handleChangeClosureCreationEthAddress(event){
    this.setState({ closureCreationEthAddress: event.target.value});
  }

  handleChangeClosureCreationClaimValue(event){
    this.setState({ closureCreationClaimValue: event.target.value });
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
    if (Object.prototype.toString.call(event.target.value) === '[object Date]'){
      this.setState({
        closureCreationStaticValue: {
          timeValue: event.target.value,
          value: '',
        },
      });
    } else {
      this.setState({
        closureCreationStaticValue: {
          timeValue: '',
          value: event.target.value,
        },
      });
    }
  };

  // Required Attributes Block
  createUIRequiredAttributes(){
    return this.state.requiredAttributes.map((el, i) => (
      <div key={i}>
        <FormControl>
          <InputLabel htmlFor={`requiredAttribute-${i}-helper`}>Required attribute</InputLabel>
          <Input
            id={`requiredAttribute-${i}`}
            value={el || ''}
            onChange={this.handleChangeRequiredAttributes.bind(this, i)}
          />
        </FormControl>
        <IconButton
          aria-label="delete"
          onClick={() => this.removeClickRequiredAttributes(i)}
          style={{ marginLeft: '15px' }}
        ><DeleteIcon/></IconButton>
        <Divider/>
      </div>
    ));
  }

  handleChangeRequiredAttributes(i, event){
    const requiredAttributes = [...this.state.requiredAttributes];
    requiredAttributes[i] = event.target.value;
    this.setState({ requiredAttributes });
  }

  addClickRequiredAttributes(){
    this.setState((prevState) => ({ requiredAttributes: [...prevState.requiredAttributes, ''] }));
  }

  removeClickRequiredAttributes(i){
    const requiredAttributes = [...this.state.requiredAttributes];
    requiredAttributes.splice(i, 1);
    this.setState({ requiredAttributes });
  }

  // Optional Attributes Block
  createUIOptionalAttributes(){
    return this.state.optionalAttributes.map((el, i) =>
      (
        <div key={i}>
          <FormControl>
            <InputLabel
              htmlFor={`optionalAttribute-${i}-helper`}
            >
              Optional attribute
            </InputLabel>
            <Input
              id={`optionalAttribute-${i}`}
              value={el || ''}
              onChange={this.handleChangeOptionalAttributes.bind(this, i)}
            />
          </FormControl>
          <IconButton
            aria-label="delete"
            onClick={() => this.removeClickOptionalAttributes(i)}
            style={{ marginLeft: '15px' }}
          >
            <DeleteIcon/>
          </IconButton>
          <Divider/>
        </div>
      )
    );
  }

  handleChangeOptionalAttributes(i, event){
    const optionalAttributes = [...this.state.optionalAttributes];
    optionalAttributes[i] = event.target.value;
    this.setState({ optionalAttributes });
  }

  addClickOptionalAttributes(){
    this.setState((prevState) => ({ optionalAttributes: [...prevState.optionalAttributes, ''] }));
  }

  removeClickOptionalAttributes(i){
    const optionalAttributes = [...this.state.optionalAttributes];
    optionalAttributes.splice(i, 1);
    this.setState({ optionalAttributes });
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
        // this.putAllMessageSeen();
      });
  }

  putAllMessageSeen() {
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
          seen: true
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

    this.cleanForm();
  }

  handleSubmitClosure(event){
    // alert('A name was submitted: ' + this.state.requiredAttributes.join(', '));
    event.preventDefault();
    this.setState({
      closureRequests: [
        {
          claimID: this.state.closureCreationClaimId,
          claimOperation: this.state.closureCreationClaimOperation,
          staticValue: this.state.closureCreationStaticValue,
        }
      ],
    });

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

  cleanForm(){
    // console.log('test', this.state);
    console.log(this.state.requiredAttributes, this.state.optionalAttributes);
    for (let i = 0; i < this.state.requiredAttributes.length; i += 1) {
      console.log(`required attribute: ${i}`);
      this.removeClickRequiredAttributes(this, i);
    }
    for (let i = 0; i < this.state.optionalAttributes.length; i += 1) {
      console.log(`optional attribute: ${i}`);
      this.removeClickOptionalAttributes(this, i);
    }
    this.setState({
      ethAddress: '',
    });
  }

  prepareClaimOutput(c){
    console.log('Preparing claim for returnString: ', c);
    let returnString = '';
    if (c.claimValue != null && c.id != null){
      if (c.claimValue.payload.value != null) {
        returnString += c.id + '(' + c.claimValue.payload.value + ')' + '; ';
      } else {
        returnString += c.id + '(' + c.claimValue.payload.timeValue + ')' + '; ';
      }
    } else {
      returnString += c.id + '(NULL)' + '; ';
    }
    return returnString;
  };

  prepareClosureOutput(c){
    let returnString = this.prepareClaimOutput(c);
    if (!c.signedClosures.isEmpty()){

    }
  };

  prepareClosureClaimOperationOutput(k, claimId){

  };

  render(){
    return (
      <article>
        <Paper>
          <section>
            <ExpansionPanel>
              <ExpansionPanelSummary>
                <Typography>Create new permission request</Typography>
              </ExpansionPanelSummary>
              <ExpansionPanelDetails
                style={{ display: 'flex', flexWrap: 'wrap'}}>
                <FormControl
                  aria-describedby="ethAddress-text"
                  style={{ marginBottom: '15px', minWidth: '60%' }}
                >
                  <InputLabel htmlFor="ethAddress-helper">Ethereum Address</InputLabel>
                  <Input id="ethAddress" value={this.state.ethAddress} onChange={this.handleChange}/>
                </FormControl>
                <Button
                  raised
                  mini
                  onClick={this.handleGetClaimsForEthAddress}
                  style={{ marginLeft: '15px' }}
                >Get Claims</Button>
                <ExpansionPanel>
                  <ExpansionPanelSummary>
                    <Typography>Required Attributes</Typography>
                  </ExpansionPanelSummary>
                  <ExpansionPanelDetails>
                    <form>
                      {this.createUIRequiredAttributes()}
                      <Button
                        fab
                        mini
                        color="primary"
                        onClick={this.addClickRequiredAttributes.bind(this)}
                      ><AddIcon/></Button>
                    </form>
                  </ExpansionPanelDetails>
                </ExpansionPanel>
                <ExpansionPanel>
                  <ExpansionPanelSummary>
                    <Typography>Optional Attributes</Typography>
                  </ExpansionPanelSummary>
                  <ExpansionPanelDetails>
                    <form>
                      {this.createUIOptionalAttributes()}
                      <Button
                        fab
                        mini
                        color="primary"
                        onClick={this.addClickOptionalAttributes.bind(this)}
                      ><AddIcon/></Button>
                    </form>
                  </ExpansionPanelDetails>
                </ExpansionPanel>
                <Button
                  raised
                  onClick={this.handleSubmit}
                  style={{ marginTop: '15px', marginLeft: '25%' }}
                >Submit</Button>
              </ExpansionPanelDetails>
            </ExpansionPanel>
          </section>
          <section>
            <ExpansionPanel>
              <ExpansionPanelSummary>
                <Typography>Create a new Closure request</Typography>
              </ExpansionPanelSummary>
              <ExpansionPanelDetails>
                <form autoComplete="off">
                  <div>
                    <FormControl
                      aria-describedby="closureCreationEthAddress-text"
                      style={{ marginBottom: '15px' }}
                    >
                      <InputLabel htmlFor="closureCreationEthAddress-helper">Ethereum Address</InputLabel>
                      <Input id="closureCreationEthAddress" value={this.state.closureCreationEthAddress} onChange={this.handleChangeClosureCreationEthAddress}/>
                    </FormControl>
                    <Button
                      raised
                      size="small"
                      onClick={this.handleGetClosuresForClaimsForEthAddress}
                      style={{ marginLeft: '15px' }}
                    >Get Claims</Button>
                  </div>
                  <FormControl style={{marginRight: '50px'}}>
                    <InputLabel htmlFor="claimId">Claim-ID</InputLabel>
                    <Select
                      value={this.state.closureCreationClaimId}
                      onChange={this.handleChangeClosureCreationClaimId}
                    >
                      <MenuItem value="">
                        <em>None</em>
                      </MenuItem>
                      {this.state.availableClaimsForClosuresForEthAddress.map((c) => (
                        <MenuItem value={c.claimID}>{c.claimID}</MenuItem>
                      ))}
                    </Select>
                  </FormControl>
                  <FormControl style={{marginRight: '50px'}}>
                    <InputLabel htmlFor="claimOperation">Claim-Operation</InputLabel>
                    <Select
                      value={this.state.closureCreationClaimOperation}
                      onChange={this.handleChangeClosureCreationClaimOperation}
                    >
                      <MenuItem value="">
                        <em>None</em>
                      </MenuItem>
                      {this.state.availableClaimsForClosuresForEthAddress.map((c) => (
                        c.claimID === this.state.closureCreationClaimId
                          ? c.claimOperations.map((o) => (
                            <MenuItem value={o}>{o}</MenuItem>
                        )) : null
                      ))}
                    </Select>
                  </FormControl>
                  <FormControl
                    aria-describedby="closureCreationClaimValue-text"
                  >
                    <InputLabel htmlFor="closureCreationClaimValue-helper">Value</InputLabel>
                    <Input id="closureCreationClaimValue" value={this.state.closureCreationClaimValue} onChange={this.handleChangeClosureCreationClaimValue}/>
                  </FormControl>
                </form>
                <Button
                  raised
                  onClick={this.handleSubmitClosure}
                  style={{ marginTop: '15px', marginLeft: '25%' }}
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
                  <div>
                    <IconButton
                      aria-label="refresh"
                      onClick={this.getNewMessages}
                    >
                      <RefreshIcon/>
                    </IconButton>
                  </div>
                  <Table>
                    <TableHead>
                      <TableRow>
                        <TableCell>ethID</TableCell>
                        <TableCell>Claims</TableCell>
                        <TableCell>Closures</TableCell>
                      </TableRow>
                    </TableHead>
                    <TableBody>
                      {this.state.tableData.map((n) => (
                        <TableRow key={n.ethId}>
                          <TableCell>{n.ethId}</TableCell>
                          <TableCell>{n.claims.map((c) => (
                            this.prepareClaimOutput(c)
                            )
                          )}</TableCell>
                        </TableRow>
                        )
                      )}
                    </TableBody>
                  </Table>
                </div>
              </ExpansionPanelDetails>
            </ExpansionPanel>
          </section>
        </Paper>
      </article>
    );
  }
}

export default Bank;
