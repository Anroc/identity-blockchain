import React from 'react';
import PropTypes from 'prop-types';
import Paper from 'material-ui/Paper';
import Table, { TableBody, TableCell, TableHead, TableRow } from 'material-ui/Table';

class PermissionRequestTable extends React.Component {
  render() {
    return (
      <Paper className="">
        <Table className="">
          <TableHead>
            <TableRow>
              <TableCell>requiredClaims</TableCell>
              <TableCell>optionalClaims</TableCell>
              <TableCell>closureRequestDTO</TableCell>
              <TableCell>requestingProvider</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {this.props.permissions.map((n) => (
              <TableRow key={n.id}>
                <TableCell>{JSON.stringify(n.requiredClaims)}</TableCell>
                <TableCell>{JSON.stringify(n.optionalClaims)}</TableCell>
                <TableCell>{n.closureRequestDTO.length > 0 ? n.closureRequestDTO[0].description : null}</TableCell>
                <TableCell>{n.requestingProvider}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </Paper>
    );
  }
}

PermissionRequestTable.propTypes = {
  permissions: PropTypes.array,
};

export default PermissionRequestTable;
