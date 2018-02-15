import React from 'react';
import PropTypes from 'prop-types';
import Button from 'material-ui/Button';
import Radio, { RadioGroup } from 'material-ui/Radio';
import { FormLabel, FormControl, FormControlLabel, FormHelperText } from 'material-ui/Form';
import ClaimSwitch from './Permissions/ClaimSwitch';
import ClosureSwitch from './Permissions/ClosureSwitch';

class PermissionForm extends React.Component {
  componentDidMount() {
  }
  render() {
    return (
      <section>
        <FormControl component="fieldset" required error>
          <div>
            <p>NEW PERMISSION</p>
          </div>
          {this.props.permission.requiredClaims && (
            <ClaimSwitch claims={this.props.permission.requiredClaims} claimType="Required" />
          )}
          {this.props.permission.optionalClaims && (
            <ClaimSwitch claims={this.props.permission.optionalClaims} claimType="Optional" />
          )}
          {this.props.permission.closureRequestDTO && (
            <ClosureSwitch closures={this.props.permission.closureRequestDTO} />
          )}
          <div>
            <br />
            Requesting Provider: {this.props.permission.requestingProvider}
            <br />
          </div>
          <FormLabel component="legend">
            Approve or deny:
          </FormLabel>
          <RadioGroup
            aria-label="Your answer:"
            name="answer"
            value={this.props.value}
            onChange={this.props.handleChange}
          >
            <FormControlLabel value="APPROVE" control={<Radio />} label="APPROVE" />
            <FormControlLabel value="DENY" control={<Radio />} label="DENY" />
          </RadioGroup>
          <FormHelperText>Please select an option.</FormHelperText>
          <Button
            raised
            color="primary"
            onClick={() => this.props.sendPermissionAnswer}
          >
            send answer
          </Button>
        </FormControl>
      </section>
    );
  }
}

PermissionForm.propTypes = {
  value: PropTypes.string,
  handleChange: PropTypes.func,
  sendPermissionAnswer: PropTypes.func,
  permission: PropTypes.object,
};

export default PermissionForm;
