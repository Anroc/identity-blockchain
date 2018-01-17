import React, { Component } from 'react';
import { Link } from 'react-router';
import PropTypes from 'prop-types';

import LoadingButton from './LoadingButton';
import { logout, clearError } from '../../actions';

class Nav extends Component {
  constructor(props) {
    super(props);
    this.logout = this.logout.bind(this);
    this.clearError = this.clearError.bind(this);
  }

  logout() {
    this.props.dispatch(logout());
  }

  clearError() {
    this.props.dispatch(clearError());
  }

  render() {
    const navButtons = this.props.loggedIn ? (
      <div>
        <Link to="/dashboard" className="btn btn--dash btn--nav">Dashboard</Link>
        <Link to="/provider" className="btn btn--dash btn--nav">Provider</Link>
        {this.props.currentlySending ? (
          <LoadingButton className="btn--nav" />
        ) : (
          <a
            href="#"
            className="btn btn--login btn--nav"
            onClick={this.logout}
          >
            Logout
          </a>
        )}
      </div>
    ) : (
      <div>
        <Link to="/register" className="btn btn--login btn--nav" onClick={this.clearError}>Register</Link>
        <Link to="/login" className="btn btn--login btn--nav" onClick={this.clearError}>Login</Link>
      </div>
    );


    return (
      <div className="nav">
        <div className="nav__wrapper">
          <Link to="/" className="nav__logo-wrapper" onClick={this.clearError}>
            <h1 className="nav__logo">IOSL&nbsp;Blockchain Identity</h1>
          </Link>
          {navButtons}
        </div>
      </div>
    );
  }
}

Nav.propTypes = {
  loggedIn: PropTypes.bool,
  currentlySending: PropTypes.bool,
  dispatch: PropTypes.func,
};

export default Nav;
