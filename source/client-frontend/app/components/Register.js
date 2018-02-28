import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import Form from './common/Form/Form';

import { registerRequest } from '../actions';
// import userRegistrationRequest from '../auth/registration/userRegistrationRequest';
import bankRegistrationRequest from '../auth/registration/bankRegistrationRequest';
import request from '../auth/request';

class Register extends Component {
  constructor(props) {
    super(props);
    this.register = this.register.bind(this);
    this.props.setEthId.bind(this);
  }

  sendRegisterRequest(password) {
    console.log('sending register request');
    const options = {
      method: 'POST',
      headers: {
        Accept: 'application/json',
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        password,
      }),
      mode: 'cors',
      credentials: 'include',
    };

    const actualRequest = request('http://srv01.snet.tu-berlin.de:1112/account/register', options)
      .then((json) => {
        console.log(`content' + ${JSON.stringify(json)}`);
        this.setState({
          ethId: json,
        });
      });
    console.log(`actual ${actualRequest}`);
  }

  /**
   * h
   * @param {string} username
   * @param {string} password
   * @param {string} accountType
   */
  register(username, password, accountType) {
    this.props.dispatch(registerRequest({ username, password, accountType, domainName: '' }));
    switch (accountType) {
      case 'user':
        this.abc(password);
        break;
      case 'bank':
        bankRegistrationRequest(password);
        break;
      default:
        break;
    }
    /*
      this.sendRegisterRequest(password);
    */
  }

  abc(password) {
    console.log('start');
    // this.props.setEthId(userRegistrationRequest(password));
    console.log('sending register request');
    const options = {
      method: 'POST',
      headers: {
        Accept: 'application/json',
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        password,
      }),
      mode: 'cors',
      credentials: 'include',
    };
    // TODO currently setting it in wrong state
    request('http://srv01.snet.tu-berlin.de:1112/account/register', options)
      .then((json) => {
        const ethId = json.ethereumID;
        console.log('ETH ID', ethId);
        console.log(`content' + ${JSON.stringify(json)}`);
        this.props.setEthId(ethId);
      });
    console.log('ETH ID');
    console.log('ETH ID', this.props.ethId);
    console.log('ETH ID2');
  }

  render() {
    const { dispatch } = this.props;
    const { formState, currentlySending, error } = this.props.data;

    return (
      <div className="form-page__wrapper">
        <div className="form-page__form-wrapper">
          <div className="form-page__form-header">
            <h2 className="form-page__form-heading">Register</h2>
          </div>
          <Form
            data={formState}
            dispatch={dispatch}
            history={this.props.history}
            onSubmit={this.register}
            btnText={'Register'}
            error={error}
            currentlySending={currentlySending}
          />
        </div>
      </div>
    );
  }
}

Register.propTypes = {
  data: PropTypes.object,
  history: PropTypes.object,
  dispatch: PropTypes.func,
  ethId: PropTypes.string,
  setEthId: PropTypes.func,
};

function select(state) {
  return {
    data: state,
  };
}

export default connect(select)(Register);
