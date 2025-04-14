import React, { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row, Card, CardBody, CardTitle, CardText, Input, Label } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getUserShops } from 'app/modules/order/order.reducer';
import { getShopDailySalaryList } from './salary.reducer';

export const Salary = () => {
  const dispatch = useAppDispatch();
  const shops = useAppSelector(state => state.orders.shops);
  const salaryList = useAppSelector(state => state.salaries.salaryList);

  const dateNow = new Date();
  const today = dateNow.toISOString().slice(0, 10);

  dateNow.setDate(dateNow.getDate() - 7);
  const sevenDaysAgo = dateNow.toISOString().slice(0, 10);

  const [selectedShopId, setSelectedShopId] = useState(-1);
  const [startDate, setStartDate] = useState(sevenDaysAgo);
  const [endDate, setEndDate] = useState(today);

  useEffect(() => {
    dispatch(getUserShops());
  }, []);

  useEffect(() => {
    if (shops.length > 0) {
      setSelectedShopId(shops[0].id);
      dispatch(getShopDailySalaryList({ shopId: shops[0].id, startDate, endDate }));
    }
  }, [shops, selectedShopId, startDate, endDate]);

  return (
    <div className="p-4">
      <h2 className="mb-4">
        <Translate contentKey="global.menu.salary">주급</Translate>
      </h2>

      <Row className="align-items-end mb-4">
        <Col md="4">
          <Label for="shopSelect">
            <Translate contentKey="global.label.shopSelect">상점 선택</Translate>
          </Label>
          <Input type="select" id="shopSelect" value={selectedShopId} onChange={e => setSelectedShopId(Number(e.target.value))}>
            {shops.map(shop => (
              <option key={shop.id} value={shop.id}>
                {shop.nameKo}
              </option>
            ))}
          </Input>
        </Col>
        <Col md="3">
          <Label for="dateSelect">
            <Translate contentKey="global.label.startDateSelect">날짜 선택</Translate>
          </Label>
          <Input id="dateSelect" defaultValue={startDate} type="date" onChange={e => setStartDate(e.target.value)} />
        </Col>
        <Col md="3">
          <Label for="dateSelect">
            <Translate contentKey="global.label.endDateSelect">날짜 선택</Translate>
          </Label>
          <Input defaultValue={endDate} type="date" onChange={e => setEndDate(e.target.value)} />
        </Col>
        <Col md="2" className="d-flex justify-content-end gap-2"></Col>
      </Row>

      <Row>
        {salaryList.map(item => (
          <Col key={item.userId} xs="6" sm="4" md="3" lg="2" className="mb-3">
            <Card className="h-100 text-center border shadow-sm">
              <CardBody className="p-2">
                <CardTitle tag="h5">
                  {item.lastName} {item.firstName}
                </CardTitle>
                <CardText className="text-muted">{item.modelNo}</CardText>
                <CardText className="text-muted">
                  <Translate contentKey="global.menu.salary">주급</Translate>:{item.salaryPrice}
                </CardText>
                <CardText className="text-muted">
                  <Translate contentKey="global.label.dayPenaltyPrice">결근 벌금</Translate>:{item.dayPenaltyPrice}
                </CardText>
                <CardText className="text-muted">
                  <Translate contentKey="global.label.timePenaltyPrice">지각 벌금</Translate>:{item.timePenaltyPrice}
                </CardText>
                <div className="d-flex flex-column gap-2">
                  <Link to={`/salary/${item.price}/${item.modelNo}`} className="btn btn-primary jh-create-entity">
                    <span className="d-md-inline">
                      <Translate contentKey="order.label.details">상세</Translate>
                    </span>
                  </Link>
                </div>
              </CardBody>
            </Card>
          </Col>
        ))}
      </Row>
    </div>
  );
};

export default Salary;
