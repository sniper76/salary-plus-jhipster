import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, FormText, Row, Table } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getShopModels } from 'app/modules/order/order.reducer';
import { createShopModels } from 'app/modules/administration/shop-management/shop-management.reducer';

export const ShopManagementUserMapping = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();

  useEffect(() => {
    handleSearchModels();
  }, [id]);

  const handleSearchModels = () => {
    dispatch(getShopModels({ shopId: id }));
  };

  const users = useAppSelector(state => state.orders.models);

  const saveModels = values => {
    const entity = {
      ...values,
      shopId: id,
    };
    console.warn('entity', entity);
    dispatch(createShopModels(entity));
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="6">
          <h1>
            <Translate contentKey="shopManagement.home.userMappingTargetLabel">연결 대상 사용자 정보</Translate>
          </h1>
          <Row className="justify-content-center">
            <Col>
              <ValidatedForm onSubmit={saveModels}>
                <FormText>성,이름,권한,모델번호 형식으로 등록하세요. ex)lee,alex,ROLE_USER,NO001</FormText>
                <ValidatedField
                  type="textarea"
                  name="csvData"
                  label="csvData"
                  validate={{
                    required: { value: true, message: 'Description is required.' },
                    maxLength: { value: 3000, message: 'Description cannot exceed 3000 characters.' },
                  }}
                  rows={10}
                />
                <Button color="primary">
                  <FontAwesomeIcon icon="save" type="submit" />
                  <Translate contentKey="entity.action.save">Save</Translate>
                </Button>
              </ValidatedForm>
            </Col>
          </Row>
        </Col>
        <Col md="6">
          <h1>
            <Translate contentKey="shopManagement.home.userMappingLabel">연결된 사용자 정보</Translate>
          </h1>
          <Table responsive striped>
            <thead>
              <tr>
                <th className="hand">
                  <Translate contentKey="global.field.id">ID</Translate>
                </th>
                <th className="hand">
                  <Translate contentKey="userManagement.firstName">FirstName</Translate>
                </th>
                <th className="hand">
                  <Translate contentKey="userManagement.lastName">lastName</Translate>
                </th>
                <th className="hand">
                  <Translate contentKey="userManagement.modelNo">modelNo</Translate>
                </th>
              </tr>
            </thead>
            <tbody>
              {users.map((user, i) => (
                <tr id={user.login} key={`user-${i}`}>
                  <td>{user.id}</td>
                  <td>{user.firstName}</td>
                  <td>{user.lastName}</td>
                  <td>{user.modelNo}</td>
                </tr>
              ))}
            </tbody>
          </Table>
          <Button tag={Link} to="/admin/shop-management" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
        </Col>
      </Row>
    </div>
  );
};

export default ShopManagementUserMapping;
