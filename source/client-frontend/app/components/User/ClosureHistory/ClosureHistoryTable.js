import React, { Component } from 'react';
import PropTypes from 'prop-types';
import Table, { TableBody, TableCell, TableHead, TableRow } from 'material-ui/Table';
import Paper from 'material-ui/Paper';

class ClosureHistoryTable extends Component {
  render() {
    return (
      <section>
        <Paper className="">
          <Table className="">
            <TableHead>
              <TableRow>
                <TableCell>Blinded Description</TableCell>
                <TableCell>Signature Generated by</TableCell>
                <TableCell>Creation Date</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {this.props.claims && this.props.claims.map((claim) => (
                claim.signedUserClosures && claim.signedUserClosures.map((closure) => (
                  <TableRow key={claim.id}>
                    <TableCell><div>{closure.blindedDescription}</div></TableCell>
                    <TableCell><div>{closure.signedClosure.payload.ethID}</div></TableCell>
                    <TableCell numeric><div>
                      {closure.signedClosure.payload.staticValue.timeValue === null && (`${closure.signedClosure.payload.staticValue.timeValue[0]}.${closure.signedClosure.payload.staticValue.timeValue[1]}.${closure.signedClosure.payload.staticValue.timeValue[2]} ${closure.signedClosure.payload.staticValue.timeValue[3]}:${closure.signedClosure.payload.staticValue.timeValue[4]}:${closure.signedClosure.payload.staticValue.timeValue[5]}`)}
                    </div></TableCell>
                  </TableRow>
                ))
              ))}
            </TableBody>
          </Table>
        </Paper>
      </section>
    );
  }
}

ClosureHistoryTable.propTypes = {
  claims: PropTypes.array,
};

export default ClosureHistoryTable;
