import React, { Component } from 'react';
import PropTypes from 'prop-types';
import Table, { TableBody, TableCell, TableHead, TableRow } from 'material-ui/Table';
import Paper from 'material-ui/Paper';
import Button from 'material-ui/Button';

class ClaimsTable extends Component {

  constructor() {
    super();
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
                  <TableCell>{n.id}</TableCell>
                  <TableCell numeric>{n.claimValue.payload}</TableCell>
                  <TableCell numeric>{n.provider.name}</TableCell>
                  <TableCell numeric>{n.claimValue.payloadType}</TableCell>
                  <TableCell>{new Date(n.modificationDate).toDateString()}</TableCell>
                  <TableCell numeric>{n.provider.ethID}</TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
          <Button
            raised
            color="primary"
            onClick={this.props.getUserClaims}
          >
            Refresh claims table
          </Button>
        </Paper>
      </section>
    );
  }
}

ClaimsTable.propTypes = {
  claims: PropTypes.array,
  getUserClaims: PropTypes.func,
};

export default ClaimsTable;
