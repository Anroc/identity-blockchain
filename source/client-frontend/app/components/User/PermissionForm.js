import React from 'react';
import PropTypes from 'prop-types';
import Button from 'material-ui/Button';
import Radio, { RadioGroup } from 'material-ui/Radio';
import { FormLabel, FormControl, FormControlLabel, FormHelperText } from 'material-ui/Form';

class PermissionForm extends React.Component {
  componentDidMount() {
  }
  render() {
    return (
      <section>
        <FormControl component="fieldset" required error>
          <FormLabel component="legend">
            Incoming Permission Request:
            <br />
            Bank wants to know: FAMILY_NAME
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
            onClick={() => console.log('clicked to put approval or denial')}
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
};

export default PermissionForm;
