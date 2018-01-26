import React, { Component } from 'react';
import Button from 'material-ui/Button';
import request from '../auth/request';

class Bank extends Component {
  constructor() {
    super();
    this.state = {
      json: null,
    };
  }

  componentDidMount() {
    console.log('TODO: give option to request permission from user');
    console.log('TODO: give section where retrieved data is displayed');
  }

  sendPermissionRequest() {
    console.log('sending register request');
    const options = {
      method: 'POST',
      headers: {
        Accept: 'application/json',
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        optionalClaims: [
        ],
        providerURL: 'http://srv01.snet.tu-berlin.de:8100',
        requiredClaims: [
          'FAMILY_NAME',
        ],
        userEthID: '',
      }),
      mode: 'cors',
      credentials: 'include',
    };

    const actualRequest = request('http://srv01.snet.tu-berlin.de:8102/permissions', options)
      .then((json) => {
        console.log(`permissions' + ${JSON.stringify(json)}`);
        this.setState({
          json,
        });
      });

    console.log(`actual ${actualRequest}`);
  }

  sendRegisterRequest() {
    console.log('sending register request');
    const options = {
      method: 'POST',
      headers: {
        Accept: 'application/json',
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        password: 'testBank',
      }),
      mode: 'cors',
      credentials: 'include',
    };

    const actualRequest = request('http://srv01.snet.tu-berlin.de:8102/register', options)
      .then((json) => {
        console.log(`permissions' + ${JSON.stringify(json)}`);
        this.setState({
          json,
        });
      });

    console.log(`actual ${actualRequest}`);
  }

  render() {
    return (
      <article>
        <section className="text-section">
          <h1>Bank</h1>
          <p>
            Hello, bank!
          </p>
          <p>
            Permission request form e.g.
            GET isOver18 from timo
          </p>
          <Button
            onClick={this.sendPermissionRequest}
          />
          <p>
            If clicked, show result
          </p>
        </section>
      </article>
    );
  }
}

export default Bank;
