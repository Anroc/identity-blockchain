import React, { Component } from 'react';
import Button from 'material-ui/Button';
import PropTypes from 'prop-types';
import ExpansionPanel, {
  ExpansionPanelDetails,
  ExpansionPanelSummary,
} from 'material-ui/ExpansionPanel';
import Typography from 'material-ui/Typography';
import ExpandMoreIcon from 'material-ui-icons/ExpandMore';

import AddIcon from 'material-ui-icons/Add';
import IconButton from 'material-ui/IconButton';
import DeleteIcon from 'material-ui-icons/Delete';
import RefreshIcon from 'material-ui-icons/Refresh';

import QRCode from './User/QR_Code';
import request from '../auth/request';
import ClaimsTable from './User/ClaimsTable';
import Welcome from './User/Welcome';
import DefaultClaims from './User/DefaultClaims';
import MessageSection from './User/MessageSection';
import PermissionsSection from './User/PermissionSection';
import PermissionForm from './User/PermissionForm';

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
      messages: [],
      permissionId: '',
      permission: null,
      permissions: [],
      expanded: null,
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
        console.log('GOT RESULT TO PERMISSION');
        console.log(JSON.stringify(json));
        const newPermissions = this.state.permissions;
        newPermissions.push(json);
        this.setState({
          permission: json,
          permissions: newPermissions,
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
        seen: true,
      }),
      credentials: 'include',
    };

    request(`http://srv01.snet.tu-berlin.de:1112/messages/${this.state.messages[0].id}`, getUserInformationOptions);
  }

  /**
   * TODO currently gives error
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
    this.getUserInformation();
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
        <ExpansionPanel expanded={true}>
          <ExpansionPanelSummary>
            <Typography>General information</Typography>
          </ExpansionPanelSummary>
          <ExpansionPanelDetails>
            <QRCode showQRCode={this.showQRCode} showQR={this.state.showQR} />
          </ExpansionPanelDetails>
        </ExpansionPanel>
        <ExpansionPanel>
          <ExpansionPanelSummary>
            <Typography>Claims</Typography>
          </ExpansionPanelSummary>
          <ExpansionPanelDetails>
            <ClaimsTable claims={this.state.claims} />
          </ExpansionPanelDetails>
        </ExpansionPanel>
        <ExpansionPanel>
          <ExpansionPanelSummary>
            <Typography>Messages</Typography>
          </ExpansionPanelSummary>
          <ExpansionPanelDetails>
            <MessageSection getMessages={this.getMessages} messages={this.state.messages} />

            <section>
              <Button
                onClick={this.putMessageSeen}
              >
                PUT auf messages, sodass seen auf true gesetzt wird
              </Button>
            </section>
            <PermissionForm handleChange={this.handleChange} value={this.state.value} />

          </ExpansionPanelDetails>
        </ExpansionPanel>
        <ExpansionPanel>
          <ExpansionPanelSummary>
            <Typography>Permissions</Typography>
          </ExpansionPanelSummary>
          <ExpansionPanelDetails>
            <PermissionsSection
              getPermissionRequest={this.getPermissionRequest}
              permissions={this.state.permissions}
            />
            <section>
              <Button
                onClick={this.putPermissionAnswer}
              >
                PUT approval oder denial auf 1112/permissions/id
              </Button>
            </section>
          </ExpansionPanelDetails>
        </ExpansionPanel>
      </article>
    );
  }
}

User.propTypes = {
  ethID: PropTypes.string,
};

export default User;
