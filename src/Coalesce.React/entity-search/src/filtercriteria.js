import React from 'react'
import {Card, CardActions, CardHeader, CardText} from 'material-ui/Card';
import FlatButton from 'material-ui/FlatButton';
import {List, ListItem} from 'material-ui/List';
import {FieldInput} from 'common-components/lib/components/FieldInput.js'
import { Row, Col } from 'react-bootstrap';

const operator_enum = [{enum: 'PropertyIsEqualTo', label: "="}, {enum: 'PropertyIsNotEqualTo', label: '!='}];


export class FilterCriteria extends React.Component {

  constructor(props) {
    super(props);

    //console.log("Filter Criteria",JSON.stringify(props.criteria), JSON.stringify(props.recordsets));

    this.state = {
      criteria: props.criteria
    }
  }

  render() {

    const { criteria } = this.state;
    var recordset;
    //console.log("FilterCriteria criteria.recordset", criteria.recordset);
    for (var ii=0; ii<this.props.recordsets.length; ii++) {
      if (criteria.recordset === this.props.recordsets[ii].name) {
        recordset = this.props.recordsets[ii];
        console.log("FilterCriteria name",this.props.recordsets[ii].name, recordset);
        break;
      }
    };
    //console.log("FilterCriteria recordsets", JSON.stringify(this.props.recordsets));
    //console.log("FilterCriteria definitions", JSON.stringify(this.props.recordsets.definition));
    return (
      <Row>
        <Col xs={3}>
          <FieldInput
            field={criteria}
            dataType="ENUMERATION_TYPE"
            attr="recordset"
            options={this.props.recordsets.map((item) => {
                return {'enum': item.name, 'label': item.name};
              })
            }
            showLabels={false}
          />
        </Col>
        <Col xs={3}>
          <FieldInput
            field={criteria}
            dataType="ENUMERATION_TYPE"
            attr="field"
            options={
                  recordset.definition.map((item) => {
                    return {'enum': item.name, 'label': item.name};
                  })
            }
            showLabels={false}
          />
        </Col>
        <Col xs={2}>
          <FieldInput
            field={criteria}
            dataType="ENUMERATION_TYPE"
            attr="operator"
            options={operator_enum}
            showLabels={false}
          />
        </Col>
        <Col xs={3}>
          <FieldInput
            field={criteria}
            dataType="STRING_TYPE"
            attr="value"
            showLabels={false}
          />
        </Col>
        <Col xs={1}>
          <FieldInput
            field={criteria}
            dataType="BOOLEAN_TYPE"
            attr="matchCase"
            showLabels={false}
          />
        </Col>
      </Row>

    )
  }

}
