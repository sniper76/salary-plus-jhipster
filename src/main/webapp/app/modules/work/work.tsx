import React, { useEffect, useState } from 'react';
import { Button, Col, Row, Card, CardBody, CardTitle, CardText, Input, Label } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import {
  createAllCheckIn,
  updateAllHalfSalary,
  createCheckIn,
  updateHalfSalary,
  getShopUsers,
  createAbsence,
} from 'app/modules/work/work.reducer';
import { getUserShops } from 'app/modules/order/order.reducer';
import { getTextByLocale } from 'app/config/translation';

export const Work = () => {
  const dispatch = useAppDispatch();
  const users = useAppSelector(state => state.works.users);
  const shops = useAppSelector(state => state.orders.shops);

  const today = new Date().toISOString().slice(0, 10);
  const [currentDate, setCurrentDate] = useState(today);
  const [selectedShopId, setSelectedShopId] = useState(-1);

  useEffect(() => {
    dispatch(getUserShops());
  }, []);

  useEffect(() => {
    if (shops.length > 0) {
      setSelectedShopId(shops[0].id);
      dispatch(getShopUsers({ shopId: shops[0].id, date: currentDate }));
    }
  }, [shops, currentDate]);

  const handleCheckInClick = user => () => {
    dispatch(createCheckIn({ shopId: selectedShopId, date: currentDate, userId: user.userId })).then(() => {
      dispatch(getShopUsers({ shopId: selectedShopId, date: currentDate }));
    });
  };

  const handleAllCheckInClick = () => {
    dispatch(createAllCheckIn({ shopId: selectedShopId, date: currentDate })).then(() => {
      dispatch(getShopUsers({ shopId: selectedShopId, date: currentDate }));
    });
  };

  const handleAbsenceClick = user => () => {
    dispatch(createAbsence({ shopId: selectedShopId, date: currentDate, userId: user.userId })).then(() => {
      dispatch(getShopUsers({ shopId: selectedShopId, date: currentDate }));
    });
  };

  const handleHalfSalaryClick = user => () => {
    dispatch(updateHalfSalary({ shopId: selectedShopId, date: currentDate, userId: user.userId })).then(() => {
      dispatch(getShopUsers({ shopId: selectedShopId, date: currentDate }));
    });
  };

  const handleAllHalfSalaryClick = () => {
    dispatch(updateAllHalfSalary({ shopId: selectedShopId, date: currentDate })).then(() => {
      dispatch(getShopUsers({ shopId: selectedShopId, date: currentDate }));
    });
  };

  return (
    <div className="p-4">
      <h2 className="mb-4">
        <Translate contentKey="global.menu.work">근무</Translate>
      </h2>

      <Row className="align-items-end mb-4">
        <Col md="4">
          <Label for="shopSelect">
            <Translate contentKey="global.label.shopSelect">상점 선택</Translate>
          </Label>
          <Input type="select" id="shopSelect" value={selectedShopId} onChange={e => setSelectedShopId(Number(e.target.value))}>
            {shops.map(shop => (
              <option key={shop.id} value={shop.id}>
                {getTextByLocale(shop.nameKo, shop.nameEn)}
              </option>
            ))}
          </Input>
        </Col>
        <Col md="3">
          <Label for="dateSelect">
            <Translate contentKey="global.label.dateSelect">날짜 선택</Translate>
          </Label>
          <Input type="date" id="dateSelect" value={currentDate} onChange={e => setCurrentDate(e.target.value)} />
        </Col>
        <Col md="5" className="d-flex justify-content-end gap-2">
          <Button color="secondary" onClick={handleAllCheckInClick}>
            <Translate contentKey="work.button.allCheckIn">전체출근</Translate>
          </Button>
          <Button color="info" onClick={handleAllHalfSalaryClick}>
            <Translate contentKey="work.button.allHalfSalary">전체 Half 일당적용하기</Translate>
          </Button>
        </Col>
      </Row>

      <Row>
        {users.map(user => (
          <Col key={user.userId} xs="6" sm="4" md="3" lg="2" className="mb-3">
            <Card className="h-100 text-center border shadow-sm">
              <CardBody className="p-2">
                <CardTitle tag="h5">
                  {user.lastName} {user.firstName}
                </CardTitle>
                <CardText className="text-muted">{user.modelNo}</CardText>
                <div className="d-flex flex-column gap-2">
                  {user.checkIn ? (
                    <>
                      <Button color="success" size="sm" disabled>
                        <Translate contentKey="work.label.checkInOk">출근완료</Translate>
                      </Button>
                      <Button color="info" size="sm" onClick={handleHalfSalaryClick(user)}>
                        <Translate contentKey="work.button.halfSalary">Half 일당적용하기</Translate>
                      </Button>
                    </>
                  ) : (
                    <Button color="secondary" size="sm" onClick={handleCheckInClick(user)}>
                      <Translate contentKey="work.button.checkIn">출근하기</Translate>
                    </Button>
                  )}
                  {user.absence ? (
                    <Button color="warning" size="sm" disabled>
                      <Translate contentKey="work.button.absence">결근</Translate>
                    </Button>
                  ) : (
                    <Button color="danger" size="sm" onClick={handleAbsenceClick(user)}>
                      <Translate contentKey="work.button.absent">결근처리</Translate>
                    </Button>
                  )}
                </div>
              </CardBody>
            </Card>
          </Col>
        ))}
      </Row>
    </div>
  );
};

export default Work;
