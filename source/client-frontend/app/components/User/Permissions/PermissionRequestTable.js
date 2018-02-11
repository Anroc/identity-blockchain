import React from 'react';
import PropTypes from 'prop-types';
import Paper from 'material-ui/Paper';
import Table, { TableBody, TableCell, TableHead, TableRow } from 'material-ui/Table';
import Switch from 'material-ui/Switch';
import { FormControlLabel, FormGroup } from 'material-ui/Form';
import ClaimSwitch from './ClaimSwitch';

class PermissionRequestTable extends React.Component {

  constructor() {
    super();
    this.state = {
      checkedA: false,
    };
  }
  render() {
    return (
      <Paper className="">
        <FormGroup>
          {this.props.permissions.map((n) => (
            <div key={n.id}>
              <div>
                <p>NEW PERMISSION</p>
              </div>
              <ClaimSwitch claims={n.requiredClaims} claimType="Required" />
              <ClaimSwitch claims={n.optionalClaims} claimType="Optional" />
              <div>
                {n.closureRequestDTO.length > 0 && n.closureRequestDTO.map((item) => (
                  <p>Closure: {item.description}</p>
                ))}
              </div>
              <div>Requesting Provider: {n.requestingProvider}</div>
              <br />
            </div>
          ))}
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
        </FormGroup>
      </Paper>
    );
  }
}

PermissionRequestTable.propTypes = {
  permissions: PropTypes.array,
};

export default PermissionRequestTable;
