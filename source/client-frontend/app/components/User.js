import React, { Component } from 'react';
import Button from 'material-ui/Button';
import PropTypes from 'prop-types';
import Table, { TableBody, TableCell, TableHead, TableRow } from 'material-ui/Table';
import Paper from 'material-ui/Paper';
import request from '../auth/request';
import LazyImage from './common/LazyImage';

class User extends Component {
  constructor() {
    super();
    this.showQRCode = this.showQRCode.bind(this);
    this.state = {
      swaggerData: '',
      ethID: '',
      qrCode: [],
      showQR: false,
      src: null,
      loaded: false,
      error: false,
      user: [],
      claims: [
        {
          modificationDate: 1515684492235,
          claimValue: {
            payloadType: 'STRING',
            payload: 'Schloessinger',
          },
          id: 'FAMILY_NAME',
          provider: {
            name: 'Bürgeramt Reinickendorf',
            ethID: '0xf1239acb6bb5e3a4a45de8cd14090c1944dce581',
          },
        },
        {
          modificationDate: 1515684492235,
          claimValue: {
            payloadType: 'DATE',
            payload: '29.2.1994',
          },
          id: 'GIVEN_NAME',
          provider: {
            name: 'Bürgeramt Reinickendorf',
            ethID: '0xf1239acb6bb5e3a4a45de8cd14090c1944dce581',
          },
        },
        {
          modificationDate: 1515684492235,
          claimValue: {
            payloadType: 'STRING',
            payload: 'Oskar',
          },
          id: 'BIRTHDAT',
          provider: {
            name: 'Bürgeramt Reinickendorf',
            ethID: '0xf1239acb6bb5e3a4a45de8cd14090c1944dce581',
          },
        },
      ],
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

    // const actualRequest = request('http://srv01.snet.tu-berlin.de:1112/claims', getUserInformationOptions)
    request('http://srv01.snet.tu-berlin.de:1112/claims', getUserInformationOptions)
      .then((json) => {
        console.log(JSON.stringify(json));
        this.setState({
          claims: json,
        });
      });
  }

  /*
   * TODO messages 1112, lookup, schaue nach id, gib
   * TODO setze claims auf true
   * TODO PUT auf permissions/id
   */
  getPermissionRequests() {
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

    // const actualRequest = request('http://srv01.snet.tu-berlin.de:1112/claims', getUserInformationOptions)
    request('http://srv01.snet.tu-berlin.de:1112/claims', getUserInformationOptions)
      .then((json) => {
        console.log(JSON.stringify(json));
        this.setState({
          claims: json,
        });
      });
  }

  showQRCode() {
    console.log('showing qr code');
    this.setState({
      showQR: true,
    });
  }

  showClaims() {
    this.getUserInformation();
  }

  // TODO proposal: you are registered but not approved yet, please scan the QR code
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
            {this.props.ethID}
          </p>
          <Button raised color="primary" onClick={this.showQRCode}>
            Show QR Code
          </Button>
          { this.state.showQR ?
            <p>
              QR Code:
              <LazyImage
                src="http://srv01.snet.tu-berlin.de:1112/account/qr-code"
                alt="qr-code"
              />
            </p> : null }
          <p>
            Claims:
          </p>

          <Paper className="">
            <Table className="">
              <TableHead>
                <TableRow>
                  <TableCell>ID</TableCell>
                  <TableCell numeric>Modification Date</TableCell>
                  <TableCell numeric>Claim Value Type</TableCell>
                  <TableCell numeric>Claim Value Payload</TableCell>
                  <TableCell numeric>Provider Name</TableCell>
                  <TableCell numeric>Provider EthID</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {this.state.claims.map((n) => (
                  <TableRow key={n.id}>
                    <TableCell>{n.id}</TableCell>
                    <TableCell>{new Date(n.modificationDate).toDateString()}</TableCell>
                    <TableCell numeric>{n.claimValue.payloadType}</TableCell>
                    <TableCell numeric>{n.claimValue.payload}</TableCell>
                    <TableCell numeric>{n.provider.name}</TableCell>
                    <TableCell numeric>{n.provider.ethID}</TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </Paper>
        </section>
      </article>
    );
  }
}

User.propTypes = {
  ethID: PropTypes.string,
};

export default User;
