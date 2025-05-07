import React, { useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Button, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { deletePenalty, getShopPenalty } from './penalty.reducer';

export const PenaltyDeleteDialog = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { shopId, shopPenaltyId } = useParams();

  useEffect(() => {
    dispatch(getShopPenalty({ shopId, shopPenaltyId }));
  }, []);

  const penalty = useAppSelector(state => state.penalties.penalty);

  const handleClose = event => {
    event.stopPropagation();
    navigate('/shop/penalty');
  };

  const confirmDelete = event => {
    dispatch(deletePenalty({ shopId, shopPenaltyId }));
    handleClose(event);
  };

  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose}>
        <Translate contentKey="entity.delete.title">Confirm delete operation</Translate>
      </ModalHeader>
      <ModalBody>
        <Translate contentKey="penalty.delete.question" interpolate={{ name: penalty.nameKo }}>
          정말로 벌금를 삭제하시겠습니까?
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

export default PenaltyDeleteDialog;
