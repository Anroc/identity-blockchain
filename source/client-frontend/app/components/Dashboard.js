import React, { Component } from 'react';
import request from '../auth/request';
import LazyImage from './common/LazyImage';
import ClaimsTable from './common/ClaimsTable';

class Dashboard extends Component {
  constructor() {
    super();
    this.state = {
      swaggerData: '',
      qrCode: [],
      src: null,
      loaded: false,
      error: false,
      user: [],
    };
  }

  componentDidMount() {
    console.log('sending request');
    this.sendRequest();
    console.log('request sent');
    this.getUserInformation();
  }

  // todo change password
  // todo error labelling
  // todo claims erst spaeter
  // todo warning no recovery possible, keep your password safe
  // todo provider user anlegen etc von 8100
  // solved research standalone version for react

  displayClaims() {
    // TODO:
    // go against /claims
    // periodic polling
  }

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
    console.log('get user information options: ', getUserInformationOptions);

    const actualRequest = request('http://srv01.snet.tu-berlin.de:8100/user', getUserInformationOptions)
      .then((json) => {
        console.log(`user: ' + ${JSON.stringify(json)}`);
        this.setState({
          user: JSON.stringify(json),
        });
        console.log(`user in state: ${this.state.swaggerData}`);
        this.getQRCode();
      });
    console.log(`actual user: ${actualRequest}`);
  }

  sendRequest() {
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
        password: 'timsDickerDick',
      }),
      mode: 'cors',
      credentials: 'include',
    };

    const actualRequest = request('http://srv01.snet.tu-berlin.de:1112/account/register', options)
      .then((json) => {
        console.log(`content' + ${JSON.stringify(json)}`);
        this.setState({
          swaggerData: JSON.stringify(json),
        });
      });
    console.log(`actual ${actualRequest}`);
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
            {JSON.stringify(this.state.swaggerData)}
          </p>
          <p>
            QR Code:
            <LazyImage
              src="http://srv01.snet.tu-berlin.de:1112/account/qr-code"
              alt="qr-code"
            />
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
  /*

  sendGetClaimsRequest() {
    const options = {
      method: 'POST',
      headers: {
        Accept: 'application/json',
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        TO_BE_DECIDED: 'random',
      }),
      mode: 'cors',
      credentials: 'include',
    };

    const actualRequest = request('http://srv01.snet.tu-berlin.de:1112/claims', options)
      .then((json) => {
        console.log(`claims content ${JSON.stringify(json)}`);
        this.setState({
          swaggerData: JSON.stringify(json),
        });
        console.log(`claims content in state: ' ${this.state.swaggerData}`);
      });
    console.log(`actual claims ${actualRequest}`);
  }

  sendLogoutRequest() {
    const options = {
      method: 'POST',
      headers: {
        Accept: 'application/json',
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({}),
      mode: 'cors',
      credentials: 'include',
    };

    const actualRequest = request('http://srv01.snet.tu-berlin.de:1112/account/logout', options)
      .then((json) => {
        console.log(`logout content ${JSON.stringify(json)}`);
        this.setState({
          swaggerData: JSON.stringify(json),
        });
        console.log(`logout content in state: ${this.state.swaggerData}`);
      });
    console.log(`actual logout ${actualRequest}`);
  }

  sendLoginRequest() {
    const options = {
      method: 'POST',
      headers: {
        Accept: 'application/json',
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        password: 'timsDickerDick',
      }),
      mode: 'cors',
      credentials: 'include',
    };

    const actualRequest = request('http://srv01.snet.tu-berlin.de:1112/account/login', options)
      .then((json) => {
        console.log(`login content ${JSON.stringify(json)}`);
        this.setState({
          swaggerData: JSON.stringify(json),
        });
        console.log(`login content in state: ${this.state.swaggerData}`);
      });
    console.log(`actual login ${actualRequest}`);
  }
   */
}

export default Dashboard;
