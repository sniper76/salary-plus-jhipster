import React, { useEffect, useState } from 'react';
import { Translate, translate, ValidatedField } from 'react-jhipster';
import { Alert, Button, Col, Form, Modal, ModalBody, ModalFooter, ModalHeader, Row } from 'reactstrap';
import { type FieldError, useForm } from 'react-hook-form';
import './order.scss';
import { useAppDispatch } from 'app/config/store';
import { IOrderDetail } from 'app/shared/model/orderDetail.model';

export interface IOrderModalRefundProps {
  showRefundModal: boolean;
  orderError: boolean;
  handleRefundOrder: (obj: any) => void;
  handleRefundClose: () => void;
  orderDetails: IOrderDetail[];
  orderId: null;
}

const OrderModalRefund = (props: IOrderModalRefundProps) => {
  const orderHandleRefundProps = obj => {
    props.handleRefundOrder(obj);
  };

  const {
    handleSubmit,
    register,
    reset,
    watch,
    formState: { errors, touchedFields },
  } = useForm({ mode: 'onTouched' });

  const dispatch = useAppDispatch();

  const { orderError, handleRefundClose } = props;

  const handleOrderRefundSubmit = handleSubmit(data => {
    orderHandleRefundProps(data);
    handleRefundClose();
  });

  const handleOrderRefundClose = () => {
    handleRefundClose();
  };

  useEffect(() => {
    reset(); // react-hook-form 의 상태도 초기화
  }, [props.orderDetails, props.orderId, reset]);

  // watch() 는 폼의 모든 값을 실시간으로 관찰합니다.
  const allValues = watch();

  useEffect(() => {
    // console.warn('Current Form Values:', allValues); // 값이 변경될 때마다 출력
  }, [allValues]);

  return (
    <Modal
      isOpen={props.showRefundModal}
      toggle={handleOrderRefundClose}
      backdrop="static"
      id="order-refund-page"
      autoFocus={false}
      className="modal-lg" // 모달 너비 키움
    >
      <Form onSubmit={handleOrderRefundSubmit}>
        <ModalHeader id="order-title" data-cy="orderTitle" toggle={handleRefundClose}>
          <Translate contentKey="global.label.refund">환불</Translate>
        </ModalHeader>
        <ModalBody>
          <Row>
            <Col md="12">
              <div className="mb-3"></div>

              <div className="d-flex gap-4">
                <div className="flex-grow-1 border rounded p-3 shadow-sm">
                  <h5 className="mb-3">
                    <Translate contentKey="order.label.details">주문 내역</Translate>
                  </h5>
                  <div className="mb-3 border rounded p-2 shadow-sm">
                    {props.orderDetails?.map((item, index) => (
                      <div key={index} className="flex justify-between items-center bg-gray-100 px-3 py-2 rounded">
                        <p>{item.nameKo}</p>
                        <p className="font-semibold">{item.price}</p>
                      </div>
                    ))}
                  </div>
                </div>

                <div className="flex-grow-1 border rounded p-3 shadow-sm">
                  <h5 className="mb-3">
                    <Translate contentKey="global.label.refund">환불</Translate>
                  </h5>
                  <div className="border rounded p-2 mb-2 shadow-sm text-left">
                    <ValidatedField
                      type="number"
                      register={register}
                      id="shopPrice"
                      name="shopPrice"
                      label={translate('order.label.shopPrice')}
                      error={errors.shopPrice as FieldError}
                      validate={{
                        required: {
                          value: true,
                          message: translate('order.messages.validate.shopPrice.required'),
                        },
                        min: { value: 0, message: translate('order.messages.validate.shopPrice.size') },
                      }}
                    />
                    <ValidatedField
                      type="number"
                      register={register}
                      id="modelPrice"
                      name="modelPrice"
                      label={translate('order.label.modelPrice')}
                      error={errors.modelPrice as FieldError}
                      validate={{
                        required: {
                          value: true,
                          message: translate('order.messages.validate.modelPrice.required'),
                        },
                        min: { value: 0, message: translate('order.messages.validate.modelPrice.size') },
                      }}
                    />
                    <ValidatedField
                      type="number"
                      register={register}
                      id="mamaPrice"
                      name="mamaPrice"
                      label={translate('order.label.mamaPrice')}
                      error={errors.mamaPrice as FieldError}
                      validate={{
                        required: {
                          value: true,
                          message: translate('order.messages.validate.mamaPrice.required'),
                        },
                        min: { value: 0, message: translate('order.messages.validate.mamaPrice.size') },
                      }}
                    />
                    <ValidatedField type="hidden" register={register} id="orderId" name="orderId" data-cy="orderId" value={props.orderId} />
                  </div>
                </div>
              </div>
            </Col>
          </Row>
        </ModalBody>
        <ModalFooter>
          <Button color="secondary" onClick={handleOrderRefundClose} tabIndex={1}>
            <Translate contentKey="entity.action.cancel">Cancel</Translate>
          </Button>
          <Button color="primary" type="submit" data-cy="submit">
            <Translate contentKey="order.button.refund">환불하기</Translate>
          </Button>
        </ModalFooter>
      </Form>
    </Modal>
  );
};

export default OrderModalRefund;
