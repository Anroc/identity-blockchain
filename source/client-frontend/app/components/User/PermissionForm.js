import React from 'react';
import PropTypes from 'prop-types';
import Button from 'material-ui/Button';
import { FormLabel, FormControl, FormHelperText } from 'material-ui/Form';
import ClaimSwitch from './Permissions/ClaimSwitch';
import ClosureSwitch from './Permissions/ClosureSwitch';
import request from '../../auth/request';

class PermissionForm extends React.Component {
  constructor() {
    super();
    this.handleLocalChange = this.handleLocalChange.bind(this);
    this.sendPermissionAnswer = this.sendPermissionAnswer.bind(this);
    this.putMessageSeen = this.putMessageSeen.bind(this);
    this.changeClaim = this.changeClaim.bind(this);
    this.changeClosure = this.changeClosure.bind(this);
    // all variables important for sending answer to endpoint
    this.state = {
      localValue: '', // approved or deny
      requiredClaims: {},
      optionalClaims: {},
      closureRequestDTO: [],
    };
  }

  componentDidMount() {
    this.updateState();
  }

  updateState() {
    this.setState({
      requiredClaims: this.props.permission.requiredClaims,
      optionalClaims: this.props.permission.optionalClaims,
      closureRequestDTO: this.props.permission.closureRequestDTO,
    });
  }

  handleLocalChange(event, value) {
    this.setState({
      localValue: value,
    });
    console.log('changing state of localValue to ', value);
  }

  putMessageSeen(messageId) {
    console.log('PUT MESSAGE TO SEEN:', messageId);
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

    request(`http://srv01.snet.tu-berlin.de:1112/messages/${messageId}`, messageSeenOptions);
  }

  sendPermissionAnswer(messageId, requiredClaims, optionalClaims, closureRequest) {
    console.log('params: ', messageId, requiredClaims, optionalClaims, closureRequest);
    console.log('messageId: ', messageId);
    console.log('required: ', requiredClaims);
    console.log('optional: ', optionalClaims);
    console.log('closure: ', closureRequest);


    console.log('in state:', this.state.requiredClaims, this.state.optionalClaims, this.state.closureRequestDTO);
    /*
    // put message seen
    this.putMessageSeen(messageId);
    // send approval with all the data to endpoint
    this.putPermissionAnswer(requiredClaims, optionalClaims, closureRequest);
    */
  }

  /**
   * TODO currently gives error
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
        id: this.state.permissionId,
        permissionRequestDTO: {
          requiredClaims,
          optionalClaims,
          closureRequestDTO,
        },
      }),
      credentials: 'include',
    };
    console.log(`Permission answer: ${JSON.stringify(getUserInformationOptions)}`);
    request(`http://srv01.snet.tu-berlin.de:1112/permissions/${this.state.permissionId}`, getUserInformationOptions);
  }

  changeClaim(claimType, key) {
    console.log('changing claim in props: ', claimType);
    console.log(key);
    if (claimType === 'Optional') {
      this.setState({
        optionalClaims: {
          [key]: !this.state.optionalClaims[key],
        },
      });
    } else {
      this.setState({
        requiredClaims: {
          [key]: !this.state.requiredClaims[key],
        },
      });
    }
    console.log('to ');
  }

  changeClosure(closureItem, index) {
    console.log('changing closure value', closureItem);
    const newClosureRequestDTO = this.state.closureRequestDTO;
    newClosureRequestDTO[index].approved = !this.state.closureRequestDTO[index].approved;
    console.log('old closure: ', this.state.closureRequestDTO);
    console.log('specific closure: ', this.state.closureRequestDTO[index]);
    console.log('approved is: ', this.state.closureRequestDTO[index].approved);
    console.log('new closure: ', newClosureRequestDTO);
    this.setState({
      closureRequestDTO: newClosureRequestDTO,
    });
  }

  render() {
    return (
      <section>
        <FormControl component="fieldset" required error>
          <FormLabel component="legend">
            New permission request:
          </FormLabel>
          {this.state.requiredClaims && (
            <ClaimSwitch
              claims={this.state.requiredClaims}
              claimType="Required"
              changeClaim={this.changeClaim}
            />
          )}
          {this.state.optionalClaims && (
            <ClaimSwitch
              claims={this.state.optionalClaims}
              claimType="Optional"
              changeClaim={this.changeClaim}
            />
          )}
          {this.state.closureRequestDTO && (
            <ClosureSwitch
              closures={this.state.closureRequestDTO}
              changeClosure={this.changeClosure}
            />
          )}
          <div>
            <br />
            Requesting Provider: {this.props.permission.requestingProvider}
            <br />
          </div>
          <FormHelperText>Please select an option.</FormHelperText>
          <Button
            raised
            color="primary"
            onClick={() => this.sendPermissionAnswer()}
          >
            send answer
          </Button>
        </FormControl>
      </section>
    );
  }
}

PermissionForm.propTypes = {
  permission: PropTypes.object,
};

export default PermissionForm;
