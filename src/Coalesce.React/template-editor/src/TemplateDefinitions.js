import React from 'react';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText'
import ActionDelete from '@material-ui/icons/Delete';
import EditorModeEdit from '@material-ui/icons/ModeEdit';
import { DialogEditDefinition } from './DialogEditDefinition';

export class Definitions extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      selected: null,
    };
  }

  handleDeleteDefinition(key) {
    this.props.onDelete(key);
  }

  render() {

    return (
      <div>
        <List>
          {this.props.data.map((item) => {return (
              <ListItem
                key={item.key}
              >
                <EditorModeEdit
                  color="primary"
                  onClick={() => {this.setState({selected: item})}}
                />
                <ListItemText primary={item.name} secondary={item.dataType} />
                <ActionDelete
                  color="primary"
                  onClick={() => {this.handleDeleteDefinition(item.key)}}
                />
              </ListItem>
          )})}
        </List>
        {this.state.selected != null &&
          <DialogEditDefinition
            opened={true}
            definition={this.state.selected}
            onClose={() => {this.setState({selected: null})}}
            onDelete={this.props.onDelete}
          />
        }
      </div>
    );
  }
}
