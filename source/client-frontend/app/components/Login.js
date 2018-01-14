import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import Form from './common/Form';

import { loginRequest } from '../actions';

class Login extends Component {
  constructor(props) {
    super(props);
    this.login = this.login.bind(this);
  }

  login(username, password, accountType) {
    this.props.dispatch(loginRequest({ username, password, accountType }));
  }

  render() {
    const { dispatch } = this.props;
    const { formState, currentlySending, error } = this.props.data;

    return (
      <div className="form-page__wrapper">
        <div className="form-page__form-wrapper">
          <div className="form-page__form-header">
            <h2 className="form-page__form-heading">Login</h2>
          </div>
          <Form
            data={formState}
            dispatch={dispatch}
            history={this.props.history}
            onSubmit={this.login}
            btnText={'Login'}
            error={error}
            currentlySending={currentlySending}
          />
        </div>
      </div>
    );
  }
}

Login.propTypes = {
  data: PropTypes.object,
  history: PropTypes.object,
  dispatch: PropTypes.func,
};

// Which props do we want to inject, given the global state?
function select(state) {
  return {
    data: state,
  };
}

// Wrap the component to inject dispatch and state into it
export default connect(select)(Login);
