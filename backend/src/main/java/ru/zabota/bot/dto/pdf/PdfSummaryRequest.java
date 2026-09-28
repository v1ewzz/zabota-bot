package ru.zabota.bot.dto.pdf;

/*
 * DTO запроса на отправку PDF-сводки в чат MAX.
 *
 * maxUserId передаётся фронтендом из WebApp.initDataUnsafe.user.id,
 * чтобы backend знал, в какой чат MAX отправить сформированный файл.
 */
public class PdfSummaryRequest {

    private Long maxUserId;

    public PdfSummaryRequest() {
    }

    public Long getMaxUserId() {
        return maxUserId;
    }

    public void setMaxUserId(Long maxUserId) {
        this.maxUserId = maxUserId;
    }
}
