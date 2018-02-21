import React, { Component } from 'react';
import Button from 'material-ui/Button';
import PropTypes from 'prop-types';
import ExpansionPanel, {
  ExpansionPanelDetails,
  ExpansionPanelSummary,
} from 'material-ui/ExpansionPanel';
import Typography from 'material-ui/Typography';
import Snackbar from 'material-ui/Snackbar';
import IconButton from 'material-ui/IconButton';
import CloseIcon from 'material-ui-icons/Close';

import QRCode from './User/QR_Code';
import request from '../auth/request';
import ClaimsTable from './User/ClaimsTable';
import DefaultClaims from './User/DefaultClaims';
import MessageSection from './User/Messages/MessageSection';
import PermissionsSection from './User/PermissionSection';
import PermissionForm from './User/PermissionForm';
import PermissionRequestTable from './User/Permissions/PermissionRequestTable';
import ClosureHistoryTable from './User/ClosureHistory/ClosureHistoryTable';

class User extends Component {
  constructor() {
    super();
    this.showQRCode = this.showQRCode.bind(this);
    this.handleChange = this.handleChange.bind(this);
    this.toggleSnack = this.toggleSnack.bind(this);
    this.getMessages = this.getMessages.bind(this);
    this.getPermissionRequest = this.getPermissionRequest.bind(this);
    this.putMessageSeen = this.putMessageSeen.bind(this);
    this.putPermissionAnswer = this.putPermissionAnswer.bind(this);
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
      claims: [],
      value: '',
      messages: [],
      permissionId: '',
      permission: null,
      permissions: [],
      expanded: null,
      snackOpen: false,
      snackMessage: '',
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

    request('http://localhost:8080/claims', getUserClaimOptions)
      .then((json) => {
        if (json !== this.state.claims) {
          console.log(JSON.stringify(json));
          this.setState({
            claims: json,
          });
        }
      });
  }

  getPermissionRequest(message) {
    this.toggleSnack('got permission request');
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

    request(`http://localhost:8080/permissions/${message.subjectID}`, options)
      .then((json) => {
        console.log('GOT RESULT TO PERMISSION');
        console.log(json);
        let uniquePermissions = this.state.permissions;
        uniquePermissions.push(json);
        uniquePermissions = uniquePermissions.filter((item, pos) => (
          uniquePermissions.indexOf(item) === pos
        ));
        this.putMessageSeen(message.subjectID);
        this.setState({
          permissions: uniquePermissions,
        });
        /*
        this.setState((prevState) => ({
          permissions: [...prevState.permissions, json],
        }));
        */
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
    request('http://localhost:8080/messages', getUserInformationOptions)
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

                this.toggleSnack('Successfully got permission requests');
                if (message.subjectID !== undefined) {
                  this.putMessageSeen(message.subjectID);
                }
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

  toggleSnack(message) {
    this.setState({
      snackOpen: true,
      snackMessage: message,
    });
  }

  putMessageSeen(permissionId) {
    if (permissionId === undefined || this.state.messages.length === 0) {
      return;
    }
    console.log('only got permissionId');

    const currentMessage = this.state.messages
      .find((message) => (message.subjectID === permissionId));
    if (!currentMessage) {
      return;
    }
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
    request(`http://localhost:8080/messages/${currentMessage.id}`, messageSeenOptions);
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
    request(`http://localhost:8080/permissions/${this.state.permissionId}`, getUserInformationOptions);
  }

  showQRCode() {
    console.log('showing qr code');
    this.setState({
      showQR: !this.state.showQR,
    });
  }

  handleChange(event, value) {
    this.setState({
      value,
    });
  }

  render() {
    return (
      <article
        style={{
          marginBottom: '20px',
        }}
      >
        <Snackbar
          anchorOrigin={{
            vertical: 'top',
            horizontal: 'center',
          }}
          open={this.state.snackOpen}
          autoHideDuration={6000}
          onClose={() => { this.setState({ snackOpen: false }); }}
          message={<span id="message-id">{this.state.snackMessage}</span>}
          action={[
            <IconButton
              key="close"
              aria-label="Close"
              color="inherit"
              onClick={() => { this.setState({ snackOpen: false }); }}
            >
              <CloseIcon />
            </IconButton>,
          ]}
        />
        <section className="text-section">
          <div><h1>Dashboard</h1></div>
        </section>
        <ExpansionPanel>
          <ExpansionPanelSummary>
            <Typography>General information</Typography>
          </ExpansionPanelSummary>
          <ExpansionPanelDetails>
            <div>
              <Typography>EthID {this.props.ethID}</Typography>
              <QRCode showQRCode={this.showQRCode} showQR={this.state.showQR} />
            </div>
          </ExpansionPanelDetails>
        </ExpansionPanel>

        <ExpansionPanel>
          <ExpansionPanelSummary>
            <Typography>My Claims</Typography>
          </ExpansionPanelSummary>
          <ExpansionPanelDetails>
            <ClaimsTable
              claims={this.state.claims}
              getUserClaims={this.getUserClaims}
              toggleSnack={this.toggleSnack}
            />
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
              toggleSnack={this.toggleSnack}
            />
          </ExpansionPanelDetails>
        </ExpansionPanel>

        <ExpansionPanel>
          <ExpansionPanelSummary>
            <Typography>Closure History</Typography>
          </ExpansionPanelSummary>
          <ExpansionPanelDetails>
            <ClosureHistoryTable
              claims={this.state.claims}
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
