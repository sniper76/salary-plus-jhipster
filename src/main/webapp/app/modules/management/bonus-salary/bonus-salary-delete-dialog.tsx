import React, { useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Button, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { deleteBonusSalary, getShopBonusSalary } from './bonus-salary.reducer';

export const BonusSalaryDeleteDialog = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { shopId, shopBonusSalaryId } = useParams();

  useEffect(() => {
    dispatch(getShopBonusSalary({ shopId, shopBonusSalaryId }));
  }, []);

  const bonusSalary = useAppSelector(state => state.bonusSalaries.bonusSalary);

  const handleClose = event => {
    event.stopPropagation();
    navigate('/shop/bonus-salary');
  };

  const confirmDelete = event => {
    dispatch(deleteBonusSalary({ shopId, shopBonusSalaryId }));
    handleClose(event);
  };

  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose}>
        <Translate contentKey="entity.delete.title">Confirm delete operation</Translate>
      </ModalHeader>
      <ModalBody>
        <Translate contentKey="userManagement.delete.question" interpolate={{ name: bonusSalary.nameKo }}>
          정말로 보너스를 삭제하시겠습니까?
        </Translate>
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp;
          <Translate contentKey="entity.action.cancel">취소</Translate>
        </Button>
        <Button color="danger" onClick={confirmDelete}>
          <FontAwesomeIcon icon="trash" />
          &nbsp;
          <Translate contentKey="entity.action.delete">삭제</Translate>
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default BonusSalaryDeleteDialog;
