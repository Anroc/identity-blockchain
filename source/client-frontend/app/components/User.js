import React, { Component } from 'react';
import Button from 'material-ui/Button';
import PropTypes from 'prop-types';
import ExpansionPanel, {
  ExpansionPanelDetails,
  ExpansionPanelSummary,
} from 'material-ui/ExpansionPanel';
import Typography from 'material-ui/Typography';

import QRCode from './User/QR_Code';
import request from '../auth/request';
import ClaimsTable from './User/ClaimsTable';
import Welcome from './User/Welcome';
import DefaultClaims from './User/DefaultClaims';
import MessageSection from './User/Messages/MessageSection';
import PermissionsSection from './User/PermissionSection';
import PermissionForm from './User/PermissionForm';
import PermissionRequestTable from './User/Permissions/PermissionRequestTable';

class User extends Component {
  constructor() {
    super();
    this.showQRCode = this.showQRCode.bind(this);
    this.handleChange = this.handleChange.bind(this);
    this.getMessages = this.getMessages.bind(this);
    this.getPermissionRequest = this.getPermissionRequest.bind(this);
    this.putMessageSeen = this.putMessageSeen.bind(this);
    this.putPermissionAnswer = this.putPermissionAnswer.bind(this);
    this.sendPermissionAnswer = this.sendPermissionAnswer.bind(this);
    this.getUserClaims = this.getUserClaims.bind(this);
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
      messages: [],
      permissionId: '',
      permission: null,
      permissions: [],
      expanded: null,
    };
  }


  componentDidMount() {
    console.log('user mounted');
    this.getMessages();
    setInterval(() => {
      this.getMessages();
    }, 10000);
  }

  componentDidUpdate() {
    console.log('component did update');
  }

  // todo change password
  // todo error labelling
  // todo claims erst spaeter requesten
  // todo alle requests erst später stellen
  // todo warning no recovery possible, keep your password safe

  /**
   * get user claims
   */
  getUserClaims() {
    const getUserClaimOptions = {
      method: 'GET',
      headers: {
        Authorization: 'Basic YWRtaW46cGVuaXNwdW1wZQ==',
        Accept: 'application/json',
        'Content-Type': 'application/json',
      },
      mode: 'cors',
      credentials: 'include',
    };

    request('http://srv01.snet.tu-berlin.de:1112/claims', getUserClaimOptions)
      .then((json) => {
        console.log(JSON.stringify(json));
        this.setState({
          claims: json,
        });
      });
  }

  getPermissionRequest(message) {
    const options = {
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
    console.log('current´s message permission id is:', message.subjectID);

    request(`http://srv01.snet.tu-berlin.de:1112/permissions/${message.subjectID}`, options)
      .then((json) => {
        console.log('GOT RESULT TO PERMISSION');
        console.log(json);
        this.setState((prevState) => ({
          permissions: [...prevState.permissions, json],
        }));
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
        console.log('get messages json: ', json);
        if (json !== this.state.messages) {
          console.log('json not equal to old messages');
          if (json.length > 0) {
            console.log('messages not empty anymore');
            json.forEach((message) => {
              console.log('requesting message: ', message);
              if (message.messageType === 'PERMISSION_REQUEST') {
                console.log('getting permission request');
                this.getPermissionRequest(message);
                this.putMessageSeen(message.subjectID);
              } else {
                console.log('getting claims');
                this.getUserClaims();
              }
            });
            this.setState({
              messages: json,
              permissionId: json[0].subjectID,
            });
          }
        }
      });
  }

  putMessageSeen(permissionId) {
    console.log('only got permissionId');
    const currentMessage = this.state.messages
      .find((message) => (message.subjectID === permissionId));

    console.log('found current message', currentMessage);
    const messageSeenOptions = {
      method: 'PUT',
      headers: {
        Authorization: 'Basic YWRtaW46cGVuaXNwdW1wZQ==',
        Accept: 'application/json',
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        seen: true,
      }),
      credentials: 'include',
    };

    request(`http://srv01.snet.tu-berlin.de:1112/messages/${currentMessage.id}`, messageSeenOptions);
  }

  sendPermissionAnswer(messageId, requiredClaims, optionalClaims, closureRequest) {
    // put message seen
    this.putMessageSeen(messageId);
    // send approval with all the data to endpoint
    this.putPermissionAnswer(requiredClaims, optionalClaims, closureRequest);
  }

  /**
   */
  putPermissionAnswer(requiredClaims, optionalClaims, closureRequestDTO) {
    const getUserInformationOptions = {
      method: 'PUT',
      headers: {
        Authorization: 'Basic YWRtaW46cGVuaXNwdW1wZQ==',
        Accept: 'application/json',
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        requiredClaims,
        optionalClaims,
        closureRequestDTO,
      }),
      credentials: 'include',
    };
    console.log(`Permission answer: ${JSON.stringify(getUserInformationOptions)}`);
    request(`http://srv01.snet.tu-berlin.de:1112/permissions/${this.state.permissionId}`, getUserInformationOptions);
  }

  showQRCode() {
    console.log('showing qr code');
    this.setState({
      showQR: !this.state.showQR,
    });
  }

  showClaims() {
    this.getUserClaims();
  }

  handleChange(event, value) {
    this.setState({
      value,
    });
  }

  handlePanel(event, expanded, panel) {
    this.setState({
      expanded: expanded ? panel : false,
    });
  }

  render() {
    return (
      <article>
        <section className="text-section">
          <Welcome ethID={this.props.ethID} />
        </section>
        <ExpansionPanel>
          <ExpansionPanelSummary>
            <Typography>General information</Typography>
          </ExpansionPanelSummary>
          <ExpansionPanelDetails>
            <QRCode showQRCode={this.showQRCode} showQR={this.state.showQR} />
          </ExpansionPanelDetails>
        </ExpansionPanel>

        <ExpansionPanel>
          <ExpansionPanelSummary>
            <Typography>My Claims</Typography>
          </ExpansionPanelSummary>
          <ExpansionPanelDetails>
            <ClaimsTable claims={this.state.claims} getUserClaims={this.getUserClaims} />
          </ExpansionPanelDetails>
        </ExpansionPanel>

        <ExpansionPanel>
          <ExpansionPanelSummary>
            <Typography>Permission Requests</Typography>
          </ExpansionPanelSummary>
          <ExpansionPanelDetails>
            <PermissionRequestTable
              permissions={this.state.permissions}
              putMessageSeen={this.putMessageSeen}
            />
          </ExpansionPanelDetails>
        </ExpansionPanel>

        {false && (
          <div>
            <ExpansionPanel>
              <ExpansionPanelSummary>
                <Typography>Messages</Typography>
              </ExpansionPanelSummary>
              <ExpansionPanelDetails>
                <div>
                  <section>
                    <Button
                      onClick={this.putMessageSeen}
                    >
                      PUT auf messages, sodass seen auf true gesetzt wird
                    </Button>
                  </section>
                </div>
                <div>
                  <PermissionForm handleChange={this.handleChange} value={this.state.value} />
                </div>
                <div>
                  <MessageSection getMessages={this.getMessages} messages={this.state.messages} />
                </div>
              </ExpansionPanelDetails>
            </ExpansionPanel>

            <ExpansionPanel>
              <ExpansionPanelSummary>
                <Typography>Permissions</Typography>
              </ExpansionPanelSummary>
              <ExpansionPanelDetails>
                <section>
                  <Button
                    onClick={this.putPermissionAnswer}
                  >
                    PUT approval oder denial auf 1112/permissions/id
                  </Button>
                </section>
                <PermissionsSection
                  getPermissionRequest={this.getPermissionRequest}
                  permissions={this.state.permissions}
                />
              </ExpansionPanelDetails>
            </ExpansionPanel>
          </div>)}
      </article>
    );
  }
}

User.propTypes = {
  ethID: PropTypes.string,
};

export default User;
