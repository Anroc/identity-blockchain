import React, { Component } from 'react';
import PropTypes from 'prop-types';
import request from '../auth/request';
import LazyImage from './common/LazyImage';
import ClaimsTable from './common/ClaimsTable';

class User extends Component {
  constructor() {
    super();
    this.state = {
      swaggerData: '',
      ethID: '',
      qrCode: [],
      src: null,
      loaded: false,
      error: false,
      user: [],
    };
  }

  componentDidMount() {
    console.log('user mounted');
    this.getUserInformation();
  }

  // todo change password
  // todo error labelling
  // todo claims erst spaeter
  // todo warning no recovery possible, keep your password safe

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
        this.setState({
          user: json,
        });
      });
  }

  render() {
    return (
      <article>
        <section className="text-section">
          <h1>Dashboard</h1>
          <p>
            Welcome, you are logged in!
          </p>
          <p>
            General:
            {this.state.ethID}
          </p>
          <p>
            QR Code:
            {this.state.ethID !== '' ?
              (<LazyImage
                src="http://srv01.snet.tu-berlin.de:1112/account/qr-code"
                alt="qr-code"
              />) : null }
          </p>
          <p>
            Claims:
          </p>
          <ClaimsTable
            user={this.state.user}
          />
          <p>
            Settings:
          </p>
        </section>
      </article>
    );
  }
}

User.propTypes = {
  ethID: PropTypes.string,
};

export default User;
