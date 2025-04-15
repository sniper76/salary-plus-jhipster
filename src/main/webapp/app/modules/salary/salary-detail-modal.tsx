import React, { useEffect } from 'react';
import { Translate } from 'react-jhipster';
import { Button, Col, Modal, ModalBody, ModalFooter, ModalHeader, Row, Card, CardBody, CardTitle, CardText } from 'reactstrap';
import { useForm } from 'react-hook-form';
import { ISalaryDetail } from 'app/shared/model/salaryDetail.model';

export interface ISalaryDetailModalProps {
  showModal: boolean;
  handleClose: () => void;
  salaryDetails: ISalaryDetail[];
}

const SalaryDetailModal = ({ showModal, handleClose, salaryDetails }: ISalaryDetailModalProps) => {
  const { reset, watch } = useForm({ mode: 'onTouched' });

  useEffect(() => {
    reset();
  }, [salaryDetails, reset]);

  const handleDetailClose = () => {
    handleClose();
  };

  return (
    <Modal isOpen={showModal} toggle={handleDetailClose} backdrop="static" size="lg" contentClassName="p-3" style={{ maxWidth: '800px' }}>
      <ModalHeader toggle={handleClose}>
        <Translate contentKey="global.menu.salary">주급</Translate>
        <Translate contentKey="global.label.details">상세</Translate>
      </ModalHeader>
      <ModalBody>
        <Row className="g-3">
          {salaryDetails.map((item, index) => (
            <Col key={index} xs="12" sm="6" md="4">
              <Card className="h-100 border shadow-sm">
                <CardBody className="p-3 d-flex flex-column justify-content-between">
                  <CardTitle tag="h6" className="fw-bold text-primary mb-2">
                    {item.date}
                  </CardTitle>
                  <div className="d-flex justify-content-between">
                    <span className="text-muted">
                      <Translate contentKey="baseSalary.home.title">일당</Translate>
                    </span>
                    <span className="fw-semibold">{item.salaryPrice?.toLocaleString()} 원</span>
                  </div>
                  <div className="d-flex justify-content-between mt-1">
                    <span className="text-muted">
                      <Translate contentKey="penalty.home.title">벌금</Translate>
                    </span>
                    <span className="fw-semibold text-danger">{item.penaltyPrice?.toLocaleString()} 원</span>
                  </div>
                </CardBody>
              </Card>
            </Col>
          ))}
        </Row>
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleDetailClose}>
          <Translate contentKey="entity.action.close">닫기</Translate>
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default SalaryDetailModal;
