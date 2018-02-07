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
              <TableCell>ID</TableCell>
              <TableCell>requestingProvider</TableCell>
              <TableCell>issuedProvider</TableCell>
              <TableCell>permissionContractAddress</TableCell>
              <TableCell>requiredClaims</TableCell>
              <TableCell>optionalClaims</TableCell>
              <TableCell>closureRequestDTO</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {this.props.permissions.map((n) => (
              <TableRow key={n.id}>
                <TableCell>{n.id}</TableCell>
                <TableCell>{n.requestingProvider}</TableCell>
                <TableCell>{n.issuedProvider}</TableCell>
                <TableCell>{n.permissionContractAddress}</TableCell>
                <TableCell>{}</TableCell>
                <TableCell>{}</TableCell>
                <TableCell>{n.closureRequestDTO[0].description}</TableCell>
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
