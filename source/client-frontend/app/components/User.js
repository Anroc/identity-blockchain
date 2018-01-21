import React, { Component } from 'react';
import PropTypes from 'prop-types';
import Table, { TableBody, TableCell, TableHead, TableRow } from 'material-ui/Table';
import Paper from 'material-ui/Paper';
import request from '../auth/request';
import LazyImage from './common/LazyImage';

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
    // this.getUserInformation();
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

    // const actualRequest = request('http://srv01.snet.tu-berlin.de:1112/claims', getUserInformationOptions)
    request('http://srv01.snet.tu-berlin.de:1112/claims', getUserInformationOptions)
      .then((json) => {
        this.setState({
          claims: json,
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
