import React from 'react';
import PropTypes from 'prop-types';
import Paper from 'material-ui/Paper';
import { FormGroup } from 'material-ui/Form';

import PermissionForm from '../PermissionForm';

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
          {this.props.permissions.map((permission) => (
            <Paper key={permission.id}>
              <PermissionForm
                permission={permission}
              />
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
