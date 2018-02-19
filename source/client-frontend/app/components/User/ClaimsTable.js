import React, { Component } from 'react';
import PropTypes from 'prop-types';
import Table, { TableBody, TableCell, TableHead, TableRow } from 'material-ui/Table';
import Paper from 'material-ui/Paper';
import Button from 'material-ui/Button';

class ClaimsTable extends Component {

  constructor() {
    super();
    this.clickClaims = this.clickClaims.bind(this);
  }

  clickClaims() {
    this.props.toggleSnack('got new user claims');
    this.props.getUserClaims();
  }

  render() {
    return (
      <section>
        <Paper className="">
          <Table className="">
            <TableHead>
              <TableRow>
                <TableCell>ID</TableCell>
                <TableCell numeric>Claim Value Payload</TableCell>
                <TableCell numeric>Provider Name</TableCell>
                <TableCell numeric>Claim Value Type</TableCell>
                <TableCell numeric>Modification Date</TableCell>
                <TableCell numeric>Provider EthID</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {this.props.claims.map((n) => (
                <TableRow key={n.id}>
                  <TableCell><div>{n.id}</div></TableCell>
                  <TableCell numeric><div>
                    {n.claimValue.payload.value !== undefined && n.claimValue.payload.value}
                    {n.claimValue.payload.value === null && (`${n.claimValue.payload.timeValue[0]}.${n.claimValue.payload.timeValue[1]}.${n.claimValue.payload.timeValue[2]} ${n.claimValue.payload.timeValue[3]}:${n.claimValue.payload.timeValue[4]}:${n.claimValue.payload.timeValue[5]}`)}
                  </div></TableCell>
                  <TableCell numeric><div>{n.provider.name}</div></TableCell>
                  <TableCell numeric><div>{n.claimValue.payloadType}</div></TableCell>
                  <TableCell><div>{new Date(n.modificationDate).toDateString()}</div></TableCell>
                  <TableCell numeric><div>{n.provider.ethID}</div></TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </Paper>
        <div style={{ margin: '20px 0' }}>
          <Button
            raised
            color="primary"
            onClick={this.clickClaims}
          >
            Refresh claims table
          </Button>

        </div>
      </section>
    );
  }
}

ClaimsTable.propTypes = {
  claims: PropTypes.array,
  getUserClaims: PropTypes.func,
  toggleSnack: PropTypes.func,
};

export default ClaimsTable;
