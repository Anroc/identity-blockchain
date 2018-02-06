/* eslint-disable semi,react/jsx-tag-spacing,jsx-quotes,space-before-blocks,space-before-function-paren,comma-dangle,no-unused-vars */
import React, { Component } from 'react';
import ExpansionPanel, {
  ExpansionPanelSummary,
  ExpansionPanelDetails,
} from 'material-ui/ExpansionPanel';
import PropTypes from 'prop-types';
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
import request from '../auth/request';

class Bank extends Component{
  constructor(){
    super();
    this.state = {
      name: '',
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
  }

  componentDidMount(){
  }

  handleChange(event){
    this.setState({ ethAddress: event.target.value });
  }

  // Required Attributes Block
  createUIRequiredAttributes(){
    return this.state.requiredAttributes.map((el, i) => (
      <div>
        <FormControl>
          <InputLabel htmlFor={`requiredAttribute-${i}-helper`}>Required attribute</InputLabel>
          <Input
            id={`requiredAttribute-${i}`}
            value={el || ''}
            onChange={() => this.handleChangeRequiredAttributes(i)}
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
        <div>
          <FormControl>
            <InputLabel
              htmlFor={`optionalAttribute-${i}-helper`}
            >
              Optional attribute
            </InputLabel>
            <Input
              id={`optionalAttribute-${i}`}
              value={el || ''}
              onChange={() => this.handleChangeOptionalAttributes(i)}
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

    request('http://srv01.snet.tu-berlin.de:8102/messages', getMessages)
      .then((json) => {
        // console.log(JSON.stringify(json));
        const tmp = json.map((message) => message.userId);
        // console.log('json:', json);
        // console.log('tmp:', tmp);
        this.setState({
          messages: json,
          userIDs: tmp,
        });
        // console.log(this.state.userIDs);
      });
    console.log(this.state.userIDs);
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
      console.log(user);
      request('http://srv01.snet.tu-berlin.de:8102/users/' + user, getUser)
        .then((json) => {
          console.log(JSON.stringify(json));
          this.setState({
            tableData: json,
          });
        });
    }
    this.putAllMessageSeen();
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
          id: message,
          updatedMessage: {
            seen: true,
          },
        }),
        credentials: 'include',
      };
      request(`http://srv01.snet.tu-berlin.de:8102/messages/${message}`, getUserInformationOptions);
    }
  }

  // Submit all attributes
  handleSubmit(event){
    // alert('A name was submitted: ' + this.state.requiredAttributes.join(', '));
    event.preventDefault();

    const postRequest = {
      optionalClaims: this.state.optionalAttributes,
      providerURL: 'http://srv01.snet.tu-berlin.de:8100',
      requiredClaims: this.state.requiredAttributes,
      userEthID: this.state.ethAddress,
    };

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
    request('http://srv01.snet.tu-berlin.de:8102/permissions', getUserInformationOptions)
      .then((json) => {
        // console.log(JSON.stringify(json));
        this.setState({
          claims: json,
        });
      }).catch((error) => {
        console.log(error);
      });

    this.cleanForm();
  }

  cleanForm(){
    console.log('test', this.state);
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

  render(){
    return (
      <article>
        <section className='text-section'>
          <h1>Bank</h1>
          <p>
            Hello, bank!
          </p>
          <p>
            Permission request form e.g.
            GET isOver18 from timo
          </p>
          <p>
            If clicked, show result
          </p>
        </section>
        <section>
          <div align='center'>
            <ExpansionPanel>
              <ExpansionPanelSummary>
                <Typography>Create new permission request</Typography>
              </ExpansionPanelSummary>
              <ExpansionPanelDetails>
                <div>
                  <div>
                    <FormControl
                      aria-describedby="ethAddress-text"
                      style={{ marginBottom: '15px' }}
                    >
                      <InputLabel htmlFor="ethAddress-helper">Ethereum Address</InputLabel>
                      <Input id="ethAddress" value={this.state.ethAddress} onChange={this.handleChange}/>
                    </FormControl>
                    <Divider/>
                  </div>
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
                          onClick={this.addClickRequiredAttributes}
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
                          onClick={this.addClickOptionalAttributes}
                        ><AddIcon/></Button>
                      </form>
                    </ExpansionPanelDetails>
                  </ExpansionPanel>
                  <Button
                    raised
                    onClick={this.handleSubmit}
                    style={{ marginTop: '15px', marginLeft: '25%' }}
                  >Submit</Button>
                </div>
              </ExpansionPanelDetails>
            </ExpansionPanel>
          </div>
        </section>
        <section>
          <ExpansionPanel>
            <ExpansionPanelSummary>
              <Typography>Granted Permissions</Typography>
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
                      <TableCell>RequestedAttributes</TableCell>
                      <TableCell>GrantedQuery</TableCell>
                      <TableCell>Status</TableCell>
                    </TableRow>
                  </TableHead>
                  <TableBody>
                    {this.state.data.map((n) => (
                      <TableRow key={n.ethID}>
                        <TableCell>{n.ethID}</TableCell>
                        <TableCell>{n.RequestedAttributes}</TableCell>
                        <TableCell numeric>{n.GrantedQuery}</TableCell>
                        <TableCell numeric>{n.Status}</TableCell>
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
