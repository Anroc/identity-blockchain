import React from 'react';
import PropTypes from 'prop-types';
import Paper from 'material-ui/Paper';
import Button from 'material-ui/Button';
import { FormGroup } from 'material-ui/Form';
import ClaimSwitch from './ClaimSwitch';
import ClosureSwitch from './ClosureSwitch';

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
            <Paper key={n.id} style={{ marginBottom: '20' }}>
              <div>
                <p>NEW PERMISSION</p>
              </div>
              {n.requiredClaims && (<ClaimSwitch claims={n.requiredClaims} claimType="Required" />)}
              {n.optionalClaims && (<ClaimSwitch claims={n.optionalClaims} claimType="Optional" />)}
              {n.closureRequestDTO && (<ClosureSwitch closures={n.closureRequestDTO} />)}
              <div>
                <br />
                Requesting Provider: {n.requestingProvider}
              </div>
            </Paper>
          ), 0)}
        </FormGroup>
      </Paper>
    );
  }
}

PermissionRequestTable.propTypes = {
  permissions: PropTypes.array,
};

export default PermissionRequestTable;
