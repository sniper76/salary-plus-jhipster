package com.salary.plus.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shiver.me.timbers.data.random.RandomLongs.somePositiveLong;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salary.plus.IntegrationTest;
import com.salary.plus.domain.Shop;
import com.salary.plus.domain.ShopBaseSalary;
import com.salary.plus.domain.ShopOrder;
import com.salary.plus.domain.ShopOrderDetail;
import com.salary.plus.domain.ShopOrderDetailDiscount;
import com.salary.plus.domain.ShopPenalty;
import com.salary.plus.domain.ShopSalesItem;
import com.salary.plus.domain.ShopSalesItemDiscount;
import com.salary.plus.domain.ShopSalesRefund;
import com.salary.plus.domain.ShopUserBaseSalaryMapping;
import com.salary.plus.domain.ShopUserDailySalary;
import com.salary.plus.domain.ShopUserMapping;
import com.salary.plus.domain.ShopUserPenaltyMapping;
import com.salary.plus.domain.ShopUserSalesSalary;
import com.salary.plus.domain.User;
import com.salary.plus.enums.DiscountType;
import com.salary.plus.enums.PenaltyType;
import com.salary.plus.enums.ShopType;
import com.salary.plus.repository.ShopBaseSalaryRepository;
import com.salary.plus.repository.ShopOrderDetailDiscountRepository;
import com.salary.plus.repository.ShopOrderDetailRepository;
import com.salary.plus.repository.ShopOrderRepository;
import com.salary.plus.repository.ShopPenaltyRepository;
import com.salary.plus.repository.ShopRepository;
import com.salary.plus.repository.ShopSalesItemDiscountRepository;
import com.salary.plus.repository.ShopSalesItemRepository;
import com.salary.plus.repository.ShopSalesRefundRepository;
import com.salary.plus.repository.ShopUserBaseSalaryMappingRepository;
import com.salary.plus.repository.ShopUserDailySalaryRepository;
import com.salary.plus.repository.ShopUserMappingRepository;
import com.salary.plus.repository.ShopUserPenaltyMappingRepository;
import com.salary.plus.repository.ShopUserSalesSalaryRepository;
import com.salary.plus.repository.UserRepository;
import com.salary.plus.security.AuthoritiesConstants;
import com.salary.plus.utils.DateTimeFormatUtil;
import com.salary.plus.utils.DateUtils;
import com.salary.plus.utils.ObjectMapperUtil;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * Integration tests for the {@link UserResource} REST controller.
 */
@AutoConfigureMockMvc
@WithMockUser(authorities = AuthoritiesConstants.ADMIN)
@IntegrationTest
class ShopPenaltyDatesResourceIT {

    private static final String TARGET_URL = "/api/shops/{shopId}/dates/{date}/orders/{orderId}/penalties";

    @Autowired
    protected ObjectMapperUtil objectMapperUtil;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private ShopUserDailySalaryRepository shopUserDailySalaryRepository;

    @Autowired
    private ShopUserBaseSalaryMappingRepository shopUserBaseSalaryMappingRepository;

    @Autowired
    private ShopBaseSalaryRepository shopBaseSalaryRepository;

    @Autowired
    private ShopUserPenaltyMappingRepository shopUserPenaltyMappingRepository;

    @Autowired
    private ShopPenaltyRepository shopPenaltyRepository;

    @Autowired
    private ShopUserMappingRepository shopUserMappingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShopOrderRepository shopOrderRepository;

    @Autowired
    private ShopOrderDetailRepository shopOrderDetailRepository;

    @Autowired
    private ShopUserSalesSalaryRepository shopUserSalesSalaryRepository;

    @Autowired
    private ShopSalesItemRepository shopSalesItemRepository;

    @Autowired
    private MockMvc restUserMockMvc;

    private MvcResult callApi(ResultMatcher resultMatcher, Long shopId, String date, Long orderId) throws Exception {
        return restUserMockMvc
            .perform(get(TARGET_URL, shopId, date, orderId).contentType(APPLICATION_JSON_VALUE).accept(APPLICATION_JSON_VALUE))
            .andExpect(resultMatcher)
            .andReturn();
    }

    public MultiValueMap<String, String> toMultiValueMap(Map<String, Object> map) {
        final MultiValueMap<String, String> linkedMultiValueMap = new LinkedMultiValueMap<>();

        map.forEach((key, value) -> {
            if (value instanceof List) {
                linkedMultiValueMap.put(key, (List) value);
            } else {
                linkedMultiValueMap.add(key, value.toString());
            }
        });

        return linkedMultiValueMap;
    }

    public <T> T getResult(MvcResult response, Class<T> responseType) throws JsonProcessingException, UnsupportedEncodingException {
        return objectMapperUtil.toResponse(response.getResponse().getContentAsString(), responseType);
    }

    @Nested
    class WhenSuccess {

        private Long shopId;
        private String date;

        private Long userId;
        private Long orderId;

        private Integer baseSalaryPrice = 500;

        private void mockData() {
            Shop shop = new Shop();
            shop.setType(ShopType.BAR);
            shop.setNameKo("shop1");
            shop.setNameEn("shop1");
            shop.setWorkStartTime("09:00:00");
            shopRepository.saveAndFlush(shop);

            final User user = userRepository.findOneByLogin("user").orElseThrow();

            shopId = shop.getId();
            userId = user.getId();
            date = DateUtils.getFormatted(DateTimeFormatUtil.yyyy_MM_dd());

            ShopUserMapping shopUserMapping = new ShopUserMapping();
            shopUserMapping.setShopId(shop.getId());
            shopUserMapping.setUserId(user.getId());
            shopUserMappingRepository.saveAndFlush(shopUserMapping);

            ShopBaseSalary shopBaseSalary = new ShopBaseSalary();
            shopBaseSalary.setShopId(shopId);
            shopBaseSalary.setPrice(baseSalaryPrice);
            shopBaseSalary.setNameKo("test name ko");
            shopBaseSalary.setNameEn("test name en");
            shopBaseSalaryRepository.saveAndFlush(shopBaseSalary);

            ShopUserBaseSalaryMapping shopUserBaseSalaryMapping = new ShopUserBaseSalaryMapping();
            shopUserBaseSalaryMapping.setUserId(userId);
            shopUserBaseSalaryMapping.setShopBaseSalaryId(shopBaseSalary.getId());
            shopUserBaseSalaryMappingRepository.saveAndFlush(shopUserBaseSalaryMapping);

            ShopUserDailySalary shopUserDailySalary = new ShopUserDailySalary();
            shopUserDailySalary.setShopId(shopId);
            shopUserDailySalary.setUserId(userId);
            shopUserDailySalary.setDate(date);
            shopUserDailySalary.setPrice(shopBaseSalary.getPrice());
            shopUserDailySalaryRepository.saveAndFlush(shopUserDailySalary);
        }

        private ShopOrderDetail mockOrderData(
            String orderDate,
            Integer longTimeBarFinePrice,
            Integer ladyDrinkPrice,
            Integer guestDrinkPrice,
            int discountPrice,
            int refundPrice
        ) {
            final Integer totalPrice = longTimeBarFinePrice + ladyDrinkPrice + guestDrinkPrice;

            ShopOrder shopOrder = new ShopOrder();
            shopOrder.setShopId(shopId);
            shopOrder.setShopTableId(somePositiveLong());
            shopOrder.setPaid(false);
            shopOrder.setDate(orderDate);
            shopOrder.setTotalPrice(totalPrice);
            shopOrder.setDiscountPrice(discountPrice);
            shopOrder.setRefundPrice(refundPrice);
            shopOrderRepository.saveAndFlush(shopOrder);

            orderId = shopOrder.getId();

            ShopSalesItem shopSalesItem1 = new ShopSalesItem();
            shopSalesItem1.setShopId(shopId);
            shopSalesItem1.setNameKo("item1 ko");
            shopSalesItem1.setNameEn("item1 en");
            shopSalesItem1.setPrice(longTimeBarFinePrice);
            shopSalesItem1.setCommissionTargetYn(true);
            shopSalesItem1.setShopCommissionPrice(0);
            shopSalesItem1.setMamaCommissionPrice(0);
            shopSalesItem1.setModelCommissionPrice(0);
            shopSalesItem1.setSnackYn(false);
            shopSalesItemRepository.saveAndFlush(shopSalesItem1);

            ShopSalesItem shopSalesItem2 = new ShopSalesItem();
            shopSalesItem2.setShopId(shopId);
            shopSalesItem2.setNameKo("item2 ko");
            shopSalesItem2.setNameEn("item2 en");
            shopSalesItem2.setPrice(ladyDrinkPrice);
            shopSalesItem2.setCommissionTargetYn(true);
            shopSalesItem2.setShopCommissionPrice(0);
            shopSalesItem2.setMamaCommissionPrice(0);
            shopSalesItem2.setModelCommissionPrice(0);
            shopSalesItem2.setSnackYn(false);
            shopSalesItemRepository.saveAndFlush(shopSalesItem2);

            ShopSalesItem shopSalesItem3 = new ShopSalesItem();
            shopSalesItem3.setShopId(shopId);
            shopSalesItem3.setNameKo("item3 ko");
            shopSalesItem3.setNameEn("item3 en");
            shopSalesItem3.setPrice(guestDrinkPrice);
            shopSalesItem3.setCommissionTargetYn(false);
            shopSalesItem3.setShopCommissionPrice(0);
            shopSalesItem3.setMamaCommissionPrice(0);
            shopSalesItem3.setModelCommissionPrice(0);
            shopSalesItem3.setSnackYn(false);
            shopSalesItemRepository.saveAndFlush(shopSalesItem3);

            ShopOrderDetail shopOrderDetail1 = new ShopOrderDetail();
            shopOrderDetail1.setShopOrderId(shopOrder.getId());
            shopOrderDetail1.setShopSalesItemId(shopSalesItem1.getId());
            shopOrderDetail1.setPrice(longTimeBarFinePrice);
            shopOrderDetailRepository.saveAndFlush(shopOrderDetail1);

            ShopOrderDetail shopOrderDetail2 = new ShopOrderDetail();
            shopOrderDetail2.setShopOrderId(shopOrder.getId());
            shopOrderDetail2.setShopSalesItemId(shopSalesItem2.getId());
            shopOrderDetail2.setPrice(ladyDrinkPrice);
            shopOrderDetailRepository.saveAndFlush(shopOrderDetail2);

            ShopOrderDetail shopOrderDetail3 = new ShopOrderDetail();
            shopOrderDetail3.setShopOrderId(shopOrder.getId());
            shopOrderDetail3.setShopSalesItemId(shopSalesItem3.getId());
            shopOrderDetail3.setPrice(guestDrinkPrice);
            shopOrderDetailRepository.saveAndFlush(shopOrderDetail3);

            ShopUserSalesSalary shopUserSalesSalary1 = new ShopUserSalesSalary();
            shopUserSalesSalary1.setShopOrderDetailId(shopOrderDetail1.getId());
            shopUserSalesSalary1.setUserId(userId);
            shopUserSalesSalary1.setPrice(longTimeBarFinePrice);
            shopUserSalesSalaryRepository.saveAndFlush(shopUserSalesSalary1);

            ShopUserSalesSalary shopUserSalesSalary2 = new ShopUserSalesSalary();
            shopUserSalesSalary2.setShopOrderDetailId(shopOrderDetail2.getId());
            shopUserSalesSalary2.setUserId(userId);
            shopUserSalesSalary2.setPrice(ladyDrinkPrice);
            shopUserSalesSalaryRepository.saveAndFlush(shopUserSalesSalary2);

            return shopOrderDetail1;
        }

        private ShopPenalty mockPenaltyData(PenaltyType penaltyType, String penaltyTypeValue, int price) {
            ShopPenalty shopPenalty = new ShopPenalty();
            shopPenalty.setShopId(shopId);
            shopPenalty.setType(penaltyType);
            shopPenalty.setTypeValue(penaltyTypeValue);
            shopPenalty.setPrice(price);
            shopPenalty.setNameKo("test ko");
            shopPenalty.setNameEn("test en");
            shopPenaltyRepository.saveAndFlush(shopPenalty);

            ShopUserPenaltyMapping shopUserPenaltyMapping = new ShopUserPenaltyMapping();
            shopUserPenaltyMapping.setShopPenaltyId(shopPenalty.getId());
            shopUserPenaltyMapping.setUserId(userId);
            shopUserPenaltyMapping.setDate(date);
            shopUserPenaltyMapping.setPrice(price);
            shopUserPenaltyMappingRepository.save(shopUserPenaltyMapping);

            return shopPenalty;
        }

        @DisplayName("필수 근무일 벌금이 있는 경우")
        @Nested
        class WhenHasMandatoryPenalty {

            private Integer penaltyPrice = 2000;

            private Integer longTimeBarFinePrice = 5000;
            private Integer ladyDrinkPrice = 350;
            private Integer guestDrinkPrice = 150;

            @BeforeEach
            void setUp() {
                mockData();
                mockOrderData(date, longTimeBarFinePrice, ladyDrinkPrice, guestDrinkPrice, 0, 0);
                mockPenaltyData(PenaltyType.MANDATORY, "FRI,SAT,SUN", penaltyPrice);
            }

            @DisplayName("정상 조회된다.")
            @Test
            @Transactional
            void create() throws Exception {
                final MvcResult mvcResult = callApi(status().isOk(), shopId, date, orderId);

                final List response = getResult(mvcResult, List.class);

                final User user = userRepository.findById(userId).orElseThrow();

                final LinkedHashMap data0 = (LinkedHashMap) response.get(0); //penalty
                assertThat(data0.get("nameKo")).isEqualTo("벌금");
                assertThat(data0.get("nameEn")).isEqualTo("penalty");
                assertThat(data0.get("orderDetailPrice")).isEqualTo(penaltyPrice);
                assertThat(data0.get("orderDetailDiscountPrice")).isEqualTo(0);
                assertThat(data0.get("modelNo")).isEqualTo(user.getModelNo());
                assertThat(data0.get("shopCommissionPrice")).isEqualTo(0);
                assertThat(data0.get("modelCommissionPrice")).isEqualTo(0);
                assertThat(data0.get("mamaCommissionPrice")).isEqualTo(0);
                assertThat(data0.get("discountShopCommissionPrice")).isEqualTo(0);
                assertThat(data0.get("discountModelCommissionPrice")).isEqualTo(0);
                assertThat(data0.get("discountMamaCommissionPrice")).isEqualTo(0);
                assertThat(data0.get("commissionTargetYn")).isEqualTo(false);
                assertThat(data0.get("snackYn")).isEqualTo(false);
            }
        }
    }
}
