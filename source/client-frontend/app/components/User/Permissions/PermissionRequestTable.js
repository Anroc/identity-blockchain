import React from 'react';
import PropTypes from 'prop-types';
import Paper from 'material-ui/Paper';
import { FormGroup } from 'material-ui/Form';
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
              {n.requiredClaims && (<ClaimSwitch claims={n.requiredClaims} claimType="Required" />)}
              {n.optionalClaims && (<ClaimSwitch claims={n.optionalClaims} claimType="Optional" />)}
              <div>
                {n.closureRequestDTO && n.closureRequestDTO.length > 0 && n.closureRequestDTO.map((item) => (
                  <p>Closure: {item.description}</p>
                ), 0)}
              </div>
              <div>Requesting Provider: {n.requestingProvider}</div>
            </div>
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
