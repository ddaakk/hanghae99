//package kr.hanghae.deploy.config
//
//import org.springframework.amqp.core.Binding
//import org.springframework.amqp.core.BindingBuilder
//import org.springframework.amqp.core.DirectExchange
//import org.springframework.amqp.core.Queue
//import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory
//import org.springframework.amqp.rabbit.connection.ConnectionFactory
//import org.springframework.amqp.rabbit.core.RabbitTemplate
//import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory
//import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer
//import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
//import org.springframework.amqp.support.converter.MessageConverter
//import org.springframework.beans.factory.annotation.Value
//import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//
//
//@Configuration
//class RabbitConfig(
//    @Value("\${rabbitmq.queue.name}")
//    private val queueName: String,
//
//    @Value("\${rabbitmq.exchange.name}")
//    private val exchangeName: String,
//
//    @Value("\${rabbitmq.routing.key}")
//    private val routingKey: String,
//
//) {
//
//    @Bean
//    fun queue(): Queue {
//        return Queue(queueName)
//    }
//
//    @Bean
//    fun exchange(): DirectExchange {
//        return DirectExchange(exchangeName)
//    }
//
//    @Bean
//    fun binding(queue: Queue, exchange: DirectExchange): Binding {
//        return BindingBuilder.bind(queue).to(exchange).with(routingKey)
//    }
//
//    @Bean
//    fun rabbitTemplate(connectionFactory: ConnectionFactory): RabbitTemplate {
//        val rabbitTemplate = RabbitTemplate(connectionFactory)
//        rabbitTemplate.messageConverter = jackson2JsonMessageConverter()
//        return rabbitTemplate
//    }
//
//    @Bean
//    fun jackson2JsonMessageConverter(): MessageConverter {
//        return Jackson2JsonMessageConverter()
//    }
//}
//
