import React, { Component } from 'react';
import { connect } from 'react-redux';
import Form from './common/Form';

import { registerRequest } from '../actions';
import request from '../auth/request';

class Register extends Component {
  constructor(props) {
    super(props);
    this.state = {
      statusCode: -1,
    }
    this.register = this.register.bind(this);
  }

  sendRequest(password) {
    const optionsForServer = {
      method: 'GET',
      headers: {
        Authorization: 'Basic YWRtaW46cGVuaXNwdW1wZQ==',
        Accept: 'application/json',
        'Content-Type': 'application/json',
      },
      mode: 'cors',
      credentials: 'include',
    };
    console.log(optionsForServer);

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

    /*
    const actualReq = fetch('http://srv01.snet.tu-berlin.de:1112/account/register', options)
      .then((response) => {
        if (response.status >= 200 && response.status < 300) {
          return response;
        }

        const error = new Error(response.statusText);
        error.response = response;
        throw error;
      })
      .then((response) => {
        if (response.status === 204 || response.status === 205) {
          return null;
        }
        return response.json();
      })
      .then(json) => {

    })
    */

    const actualRequest = request('http://srv01.snet.tu-berlin.de:1112/account/register', options)
      .then((json) => {
        console.log(`content' + ${JSON.stringify(json)}`);
        this.setState({
          swaggerData: JSON.stringify(json),
        });
        console.log(`content in state: ${this.state.swaggerData}`);
      });
    console.log(`actual ${actualRequest}`);
  }

  register(username, password) {
    this.props.dispatch(registerRequest({ username, password }));
    this.sendRequest(password);
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
  data: React.PropTypes.object,
  history: React.PropTypes.object,
  dispatch: React.PropTypes.func,
};

function select(state) {
  return {
    data: state,
  };
}

export default connect(select)(Register);
