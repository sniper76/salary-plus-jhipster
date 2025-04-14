import React, { useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Button, Modal, ModalBody, ModalFooter, ModalHeader, Col, Row } from 'reactstrap';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getShopPenalties } from 'app/modules/management/penalty/penalty.reducer';

export const WorkAttendanceDialog = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { shopId, userId, date } = useParams();

  useEffect(() => {
    dispatch(getShopPenalties({ id: parseInt(shopId, 10) }));
  }, []);

  const handleClose = event => {
    event.stopPropagation();
    navigate('/work');
  };

  const penalties = useAppSelector(state => state.penalties.penalties);
  console.warn('shopId, userId, date', shopId, userId, date, penalties);

  const confirmSave = event => {
    // dispatch(deleteUser(userId));
    handleClose(event);
  };

  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose}>
        <Translate contentKey="work.button.attendance">근태관리</Translate>
      </ModalHeader>
      <ModalBody>
        <Row>
          {penalties.map((item, idx) => (
            <Col key={idx} xs="auto" className="border text-center p-3">
              <input type="checkbox" className="form-check-input" value={item.id} />
              <p>
                {item.nameKo} {item.nameEn}
              </p>
            </Col>
          ))}
        </Row>
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp;
          <Translate contentKey="entity.action.cancel">Cancel</Translate>
        </Button>
        <Button color="danger" onClick={confirmSave}>
          <FontAwesomeIcon icon="trash" />
          &nbsp;
          <Translate contentKey="entity.action.save">Save</Translate>
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default WorkAttendanceDialog;
