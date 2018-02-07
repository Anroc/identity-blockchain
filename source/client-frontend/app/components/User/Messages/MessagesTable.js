import React from 'react';
import PropTypes from 'prop-types';
import Paper from 'material-ui/Paper';
import Table, { TableBody, TableCell, TableHead, TableRow } from 'material-ui/Table';

class MessagesTable extends React.Component {
  render() {
    return (
      <Paper className="">
        <Table className="">
          <TableHead>
            <TableRow>
              <TableCell>ID</TableCell>
              <TableCell numeric>messageType</TableCell>
              <TableCell numeric>creationDate</TableCell>
              <TableCell numeric>subjectID</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {this.props.messages.map((n) => (
              <TableRow key={n.id}>
                <TableCell>{n.id}</TableCell>
                <TableCell numeric>{n.messageType}</TableCell>
                <TableCell numeric>{n.creationDate}</TableCell>
                <TableCell numeric>{n.subjectID}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </Paper>
    );
  }
}

MessagesTable.propTypes = {
  messages: PropTypes.array,
};

export default MessagesTable;
