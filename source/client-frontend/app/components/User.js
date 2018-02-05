import React, { Component } from 'react';
import Button from 'material-ui/Button';
import PropTypes from 'prop-types';
import Radio, { RadioGroup } from 'material-ui/Radio';
import { FormLabel, FormControl, FormControlLabel, FormHelperText } from 'material-ui/Form';
import QRCode from './User/QR_Code';
import request from '../auth/request';
import ClaimsTable from './User/ClaimsTable';
import Welcome from './User/Welcome';
import DefaultClaims from './User/DefaultClaims';
import MessageSection from './User/MessageSection';
import PermissionsSection from './User/PermissionSection';

class User extends Component {
  constructor() {
    super();
    this.showQRCode = this.showQRCode.bind(this);
    this.handleChange = this.handleChange.bind(this);
    this.getMessages = this.getMessages.bind(this);
    this.getPermissionRequest = this.getPermissionRequest.bind(this);
    this.putMessageSeen = this.putMessageSeen.bind(this);
    this.putPermissionAnswer = this.putPermissionAnswer.bind(this);
    this.state = {
      swaggerData: '',
      ethID: '',
      qrCode: [],
      showQR: false,
      src: null,
      loaded: false,
      error: false,
      user: [],
      claims: DefaultClaims,
      value: '',
      messages: null,
      permissionId: '',
      permission: null,
    };
  }


  componentDidMount() {
    console.log('user mounted');
  }

  // todo change password
  // todo error labelling
  // todo claims erst spaeter requesten
  // todo alle requests erst später stellen
  // todo warning no recovery possible, keep your password safe

  /**
   * get user claims
   */
  getUserInformation() {
    const getUserInformationOptions = {
      method: 'GET',
      headers: {
        Authorization: 'Basic YWRtaW46cGVuaXNwdW1wZQ==',
        Accept: 'application/json',
        'Content-Type': 'application/json',
      },
      mode: 'cors',
      credentials: 'include',
    };

    request('http://srv01.snet.tu-berlin.de:1112/claims', getUserInformationOptions)
      .then((json) => {
        console.log(JSON.stringify(json));
        this.setState({
          claims: json,
        });
      });
  }

  getPermissionRequest() {
    const getUserInformationOptions = {
      method: 'GET',
      headers: {
        Authorization: 'Basic YWRtaW46cGVuaXNwdW1wZQ==',
        Accept: 'application/json',
        'Content-Type': 'application/json',
      },
      mode: 'cors',
      credentials: 'include',
    };
    console.log('GET PERMISSION REQUEST');
    console.log('current permissions id is:', this.state.permissionId);

    request(`http://srv01.snet.tu-berlin.de:1112/permissions/${this.state.permissionId}`, getUserInformationOptions)
      .then((json) => {
        console.log('GOT RESULT');
        console.log(JSON.stringify(json));
        this.setState({
          permission: json,
        });
      });
  }

  getMessages() {
    const getUserInformationOptions = {
      method: 'GET',
      headers: {
        Authorization: 'Basic YWRtaW46cGVuaXNwdW1wZQ==',
        Accept: 'application/json',
        'Content-Type': 'application/json',
      },
      mode: 'cors',
      credentials: 'include',
    };
    console.log('GET MESSAGES');
    request('http://srv01.snet.tu-berlin.de:1112/messages', getUserInformationOptions)
      .then((json) => {
        console.log(JSON.stringify(json));
        this.setState({
          messages: json,
          permissionId: json[0].subjectID,
        });
      });
  }

  /**
   * TODO
   */
  putMessageSeen() {
    console.log('PUT MESSAGE TO SEEN:', this.state.permissionId);
    const getUserInformationOptions = {
      method: 'PUT',
      headers: {
        Authorization: 'Basic YWRtaW46cGVuaXNwdW1wZQ==',
        Accept: 'application/json',
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        id: this.state.permissionId,
        updatedMessage: {
          seen: true,
        },
      }),
      credentials: 'include',
    };

    request(`http://srv01.snet.tu-berlin.de:1112/messages/${this.state.permissionId}`, getUserInformationOptions);
  }

  /**
   * TODO
   */
  putPermissionAnswer() {
    const getUserInformationOptions = {
      method: 'PUT',
      headers: {
        Authorization: 'Basic YWRtaW46cGVuaXNwdW1wZQ==',
        Accept: 'application/json',
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        id: this.state.permissionId,
        permissionRequestDTO: {
          requiredClaims: {},
          optionalClaims: {},
        },
      }),
      credentials: 'include',
    };

    request(`http://srv01.snet.tu-berlin.de:1112/permissions/${this.state.permissionId}`, getUserInformationOptions);
  }

  showQRCode() {
    console.log('showing qr code');
    this.setState({
      showQR: true,
    });
  }

  showClaims() {
    this.getUserInformation();
  }

  handleChange(event, value) {
    this.setState({
      value,
    });
  }

  // TODO proposal: you are registered but not approved yet, please scan the QR code
  render() {
    return (
      <article>
        <section className="text-section">
          <Welcome ethID={this.props.ethID} />
          <QRCode showQRCode={this.showQRCode} showQR={this.state.showQR} />
          <ClaimsTable claims={this.state.claims} />
        </section>
        <MessageSection getMessages={this.getMessages} messages={this.state.messages} />
        <PermissionsSection getPermissionRequest={this.getPermissionRequest} />
        <section>
          <Button
            onClick={this.putMessageSeen}
          >
            PUT auf messages, sodass seen auf true gesetzt wird
          </Button>
        </section>
        <section>
          <Button
            onClick={this.putPermissionAnswer}
          >
            PUT approval oder denial auf 1112/permissions/id
          </Button>
          <p>
            test
          </p>
        </section>
        <section>
          <FormControl component="fieldset" required error>
            <FormLabel component="legend">
              Incoming Permission Request:
              <br />
              Bank wants to know: FAMILY_NAME
            </FormLabel>
            <RadioGroup
              aria-label="Your answer:"
              name="answer"
              value={this.state.value}
              onChange={this.handleChange}
            >
              <FormControlLabel value="APPROVE" control={<Radio />} label="APPROVE" />
              <FormControlLabel value="DENY" control={<Radio />} label="DENY" />
            </RadioGroup>
            <FormHelperText>Please select an option.</FormHelperText>
            <Button
              raised
              color="primary"
              onClick={console.log('clicked to put approval or denial')}
            >
              send answer
            </Button>
          </FormControl>
        </section>
      </article>
    );
  }
}

User.propTypes = {
  ethID: PropTypes.string,
};

export default User;
